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
            Pattern.compile("-(.+)\\.\\w+$");

    int counter;
    int day;
    String place;
    String description;
    String identifier;

    public static Optional<String> getIdentifierFromFilename(String filename) {
        Matcher matcher = IDENTIFIER_FROM_FILENAME_PATTERN.matcher(filename);
        if (matcher.find()) {
            return Optional.ofNullable(matcher.group());
        } else {
            return Optional.empty();
        }
    }

    public Photo validate() {
        if (counter < 1 ||
                day < 0 ||
                place.contains("-") ||
                description.contains("-") ||
                identifier.contains("-")
        ) {
            throw new RuntimeException("Illegal Photo: " + this);
        }
        return this;
    }

    public String toFileName() {
        return String.format(
                "%04d-%02d-%s-%s-%s.jpg",
                counter,
                day,
                place,
                description,
                identifier);
    }
}
