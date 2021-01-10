package adfa.photorename;

import adfa.photorename.model.Outcome;
import adfa.photorename.model.Photo;
import adfa.photorename.model.Renaming;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorTest {

    private final Processor processor = new Processor();

    @Test
    void testProcess() {

        // prepare
        List<Photo> photos = List.of(
                createPhoto(2, "DSC001"),
                createPhoto(1, "DSC003"),
                createPhoto(5, "DSC005"),
                createPhoto(4, "DSC002"),
                createPhoto(3, "DSC004")
        );

        List<String> files = List.of(
                "map.png",
                "DSC001.jpg",
                "001-DSC004.jpg",
                "001-abc-DSC002.JPG",
                "001-abc-DSC005.jpg",
                "DSC003.jpeg",
                "DSC111.jpeg",
                "index.csv"
        );

        // test
        Outcome result = processor.process(photos, files);

        // verify
        assertEquals(List.of(
                Renaming.builder().oldName("DSC003.jpeg").newName("0001-03-some-where-DSC003.jpg").build(),
                Renaming.builder().oldName("DSC001.jpg").newName("0002-03-some-where-DSC001.jpg").build(),
                Renaming.builder().oldName("001-DSC004.jpg").newName("0003-03-some-where-DSC004.jpg").build(),
                Renaming.builder().oldName("001-abc-DSC002.JPG").newName("0004-03-some-where-DSC002.jpg").build(),
                Renaming.builder().oldName("001-abc-DSC005.jpg").newName("0005-03-some-where-DSC005.jpg").build()
        ), result.getRenamings());

        assertEquals(List.of(
//                "index.csv", completely ignored, doesn't match file name pattern
//                "map.png",
                "DSC111.jpeg"
        ), result.getOtherIgnoredFiles());
    }

    @Test
    void testProcessMissingFile() {
        // prepare
        List<Photo> photos = List.of(
                createPhoto(2, "DSC001"),
                createPhoto(1, "DSC003")
        );

        List<String> files = List.of("DSC001.jpg");

        // test
        assertThrows(RuntimeException.class, () -> processor.process(photos, files));
    }

    @Test
    void testProcessIllegalIdentifier() {
        // prepare
        List<Photo> photos = List.of(
                createPhoto(2, "DSC-001")
        );

        List<String> files = List.of("DSC001.jpg", "DSC-001.jpg", "001.jpg");

        // test
        assertThrows(RuntimeException.class, () -> processor.process(photos, files));
    }

    private Photo createPhoto(int counter, String identifier) {
        return Photo.builder()
                .counter(counter)
                .identifier(identifier)
                .day(3)
                .place("some")
                .description("where")
                .build();
    }
}