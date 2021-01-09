package adfa.photorename;

import adfa.photorename.model.Photo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvParserTest {

    private CsvParser parser = new CsvParser();

    @Test
    void parseSuccessful() {
        // prepare
        String input = "" +
                "File;Day;Place;Description\n" +
                "DSC1.jpg;3,Hwy1;Bridge\n" +
                "DSC55.jpg ; 2 ; Hwy1 ; Bridge_1\n" +
                " 123_456.jpg , 5 , Hwy1 , Bridge_1 ";
        InputStream is = toInputStream(input);

        // test
        List<Photo> result = parser.parse(is);

        // verify
        assertEquals(List.of(
                Photo.builder().counter(1).day(3).place("Hwy1").description("Bridge").identifier("DSC1").build(),
                Photo.builder().counter(2).day(2).place("Hwy1").description("Bridge_1").identifier("DSC55").build(),
                Photo.builder().counter(3).day(5).place("Hwy1").description("Bridge_1").identifier("123_456").build()
                ), result);
    }

    @Test
    void parseSuccessfulWithMissingFields() {
        // prepare
        String input = "" +
                "File;Day;Place;Description\n" +
                "DSC1.jpg;3,\t;Bridge\n" +
                "DSC55.jpg ; 2 ; Hwy1 ; \n" +
                " 123_456.jpg , 5\n" +
                " 123_456.jpg , 5 ;;;; other , crap \n";
        InputStream is = toInputStream(input);

        // test
        List<Photo> result = parser.parse(is);

        // verify
        assertEquals(List.of(
                Photo.builder().counter(1).day(3).place("").description("Bridge").identifier("DSC1").build(),
                Photo.builder().counter(2).day(2).place("Hwy1").description("").identifier("DSC55").build(),
                Photo.builder().counter(3).day(5).place("").description("").identifier("123_456").build(),
                Photo.builder().counter(4).day(5).place("").description("").identifier("123_456").build()
        ), result);
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "DSC1.jpg", // missing day
                    "DSC1.jpg;", // missing day
                    ",1", // missing identifier
                    "DSC1.png;3,Hwy1;Bridge", // unsupported type
                    "DSC1.jpg;a,Hwy1;Bridge", // day NaN
            }
    )
    void parseFailing(String csvRow) {
        // prepare
        String input = "File;Day;Place;Description\n" + csvRow;
        InputStream is = toInputStream(input);

        // test
        assertThrows(RuntimeException.class, () -> parser.parse(is));
    }


    private InputStream toInputStream(String string) {
        return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
    }

}