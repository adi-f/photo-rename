package adfa.photorename.model;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Value
@Builder
public class Photo {
    private static final Pattern IDENTIFIER_FROM_FILENAME_PATTERN =
            Pattern.compile("(?i)-?([^-]+)\\.(:?jpg|jpeg)$");

    int counter;
    int day;
    String place;
    String description;
    String identifier;

    public static Optional<String> getIdentifierFromFilename(String filename) {
        Matcher matcher = IDENTIFIER_FROM_FILENAME_PATTERN.matcher(filename);
        if (matcher.find()) {
            return Optional.ofNullable(matcher.group(1));
        } else {
            return Optional.empty();
        }
    }

    public Photo validate() {
        if (counter < 1 ||
                day < 0 ||
                place.contains("-") || place.contains(" ") ||
                description.contains("-") || description.contains(" ") ||
                identifier.isEmpty() || identifier.contains("-") || identifier.contains(" ")
        ) {
            throw new RuntimeException("Illegal Photo: " + this);
        }
        return this;
    }

    public String toFileName() {
        StringBuilder filename = new StringBuilder(String.format("%04d-%02d", counter, day));
        if(!place.isEmpty()) {
            filename.append('-').append(place);
        }
        if(!description.isEmpty()) {
            filename.append('-').append(description);

        }
        filename.append('-').append(identifier).append(".jpg");
        return filename.toString();
    }
}
