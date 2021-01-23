package adfa.photorename;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ExifDateTakenComparator implements Comparator<File> {
    // YYY:MM:DD hh:mm:ss (no timezone, no ms)
    private static final Pattern DATE_TAKEN_FORMAT = Pattern.compile("^\\d{4}:\\d{2}:\\d{2} \\d{2}:\\d{2}:\\d{2}$");

    private Map<File, String> timestampCache = new HashMap<>();

    @Override
    public int compare(File file1, File file2) {
        return readDateTakenCaching(file1).compareTo(readDateTakenCaching(file2));
    }

    private String readDateTakenCaching(File file) {
        return timestampCache.computeIfAbsent(file, this::readDateTaken);
    }

    private String readDateTaken(File file) {
        try {
            JpegImageMetadata metadata = (JpegImageMetadata) Imaging.getMetadata(file);
            String dateTaken = metadata.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL).getStringValue();
            validateTimestamp(dateTaken);
            return dateTaken;
        } catch (Exception e) {
            throw new RuntimeException("Error reading 'date taken' from " + file.getName(), e);
        }
    }

    private void validateTimestamp(String timestamp) {
        if (!DATE_TAKEN_FORMAT.matcher(timestamp).matches()) {
            throw new RuntimeException("Illegal 'date take' timestamp format: " + timestamp);
        }
    }
}
