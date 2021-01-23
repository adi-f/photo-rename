package adfa.photorename;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExifDateTakenComparatorTest {

    Comparator<File> testee = new ExifDateTakenComparator();

    @Test
    void testCompare() throws Exception {

        // prepare
        List<File> expected = List.of(toFile("DSC01796.JPG"),toFile("DSC01837.JPG"));
        List<File> unsorted = Arrays.asList(toFile("DSC01837.JPG"),toFile("DSC01796.JPG"));
        List<File> alreadySorted = Arrays.asList(toFile("DSC01796.JPG"),toFile("DSC01837.JPG"));

        // test
        unsorted.sort(testee);
        alreadySorted.sort(testee);

        // verify
        assertEquals(expected, unsorted);
        assertEquals(expected, alreadySorted);
    }

    File toFile(String fileName) throws URISyntaxException {
        URL url = this.getClass().getClassLoader().getResource("images/" + fileName);
        return new File(url.toURI());
    }
}