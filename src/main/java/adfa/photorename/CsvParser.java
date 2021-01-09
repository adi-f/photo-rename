package adfa.photorename;

import adfa.photorename.model.Photo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CsvParser {

    private static final Pattern SEPARATOR = Pattern.compile("[,;]");

    public List<Photo> parse(InputStream is) {
        List<String> lines = readLines(is);
        List<List<String>> table = toTableOfValues(lines);
        return toPhotos(table);
    }

    private List<String> readLines(InputStream is) {
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(is);
        scanner.nextLine(); // skip header
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }

        if (lines.get(lines.size() - 1).isBlank()) {
            lines.remove(lines.size() - 1);
        }
        return lines;
    }

    private List<List<String>> toTableOfValues(List<String> lines) {
        return lines.stream()
                .map(line -> SEPARATOR.splitAsStream(line).map(String::trim).collect(Collectors.toUnmodifiableList()))
                .peek(line -> {
                    if (line.size() < 2) throw new RuntimeException("incomplete row: " + line);
                })
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Photo> toPhotos(List<List<String>> table) {
        List<Photo> photos = new ArrayList<>(table.size());
        for (int i = 0; i < table.size(); i++) {
            List<String> row = table.get(i);
            photos.add(Photo.builder()
                    .counter(i + 1)
                    .day(Integer.parseInt(row.get(1)))
                    .place(getOrEmpty(row, 2))
                    .description(getOrEmpty(row, 3))
                    .identifier(getIdentifier(row.get(0)))
                    .build()
                    .validate());
        }
        return photos;
    }

    private String getOrEmpty(List<String> row, int index) {
        if(index < row.size()) {
            return row.get(index);
        } else {
            return "";
        }
    }

    private String getIdentifier(String filename) {
        return Photo.getIdentifierFromFilename(filename).orElseThrow(() -> new RuntimeException("Invalid filename in CSV: " + filename));
    }
}
