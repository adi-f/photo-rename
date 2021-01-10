package adfa.photorename.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PhotoTest {

    @Test
    void testGetIdentifierFromFilename() {
        assertEquals(Optional.of("DSC123"), Photo.getIdentifierFromFilename("DSC123.jpg"));
        assertEquals(Optional.of("DSC123"), Photo.getIdentifierFromFilename("DSC123.JPEG"));
        assertEquals(Optional.of("DSC123"), Photo.getIdentifierFromFilename("DSC123.jPg"));
        assertEquals(Optional.of("id"), Photo.getIdentifierFromFilename("123-any-thing-before-the-id.JPEG"));
        assertEquals(Optional.empty(), Photo.getIdentifierFromFilename("hello"));
        assertEquals(Optional.empty(), Photo.getIdentifierFromFilename(""));
        assertEquals(Optional.empty(), Photo.getIdentifierFromFilename("."));
        assertEquals(Optional.empty(), Photo.getIdentifierFromFilename(".."));
        assertEquals(Optional.empty(), Photo.getIdentifierFromFilename("map.png"));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2, Hwy5, Bridge, 123_456, true",
            "1, 0, Hwy_5, Bridge_1, DSC132, true",
            "1, 1, '', '', DSC132, true",
            "0, 2, Hwy5, Bridge, DSC123, false",
            "1, -1, Hwy5, Bridge, DSC123, false",
            "1, 1, Hwy 5, Bridge, DSC123, false",
            "1, 1, Hwy5, Pretty Bridge, DSC123, false",
            "1, 1, Hwy-5, Bridge, DSC123, false",
            "1, 1, Hwy5, Nice-Bridge, DSC123, false",
            "1, 1, Hwy5, Bridge, '', false",
            "1, 1, Hwy5, Bridge, DSC-123, false"

    })
    void testValidate(int counter, int day, String place, String description, String identifier, boolean valid) {
        Photo photo = Photo.builder()
                .counter(counter)
                .day(day)
                .place(place)
                .description(description)
                .identifier(identifier)
                .build();

        if(valid) {
            photo.validate();
        } else {
            assertThrows(RuntimeException.class, photo::validate);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2, Hwy5, Bridge, 123_456, 0001-02-Hwy5-Bridge-123_456.jpg",
            "0, 0, DownTown, RoseField, DSC123, 0000-00-DownTown-RoseField-DSC123.jpg",
            "123, 18, DownTown, RoseField, DSC123, 0123-18-DownTown-RoseField-DSC123.jpg",
            "9999, 99, DownTown, RoseField, DSC123, 9999-99-DownTown-RoseField-DSC123.jpg",
            "1234, 5, DownTown, '', DSC123, 1234-05-DownTown-DSC123.jpg",
    })
    void testToFileName(int counter, int day, String place, String description, String identifier, String expected) {
        Photo photo = Photo.builder()
                .counter(counter)
                .day(day)
                .place(place)
                .description(description)
                .identifier(identifier)
                .build();

        assertEquals(expected, photo.toFileName());
    }
}