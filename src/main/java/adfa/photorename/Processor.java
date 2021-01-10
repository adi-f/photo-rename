package adfa.photorename;

import adfa.photorename.model.Outcome;
import adfa.photorename.model.Photo;
import adfa.photorename.model.Renaming;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Processor {

    public Outcome process(List<Photo> photos, List<String> photoFiles) {
        Map<String, Photo> identifierToPhoto = toIdentifierToPhotoMap(photos);
        Map<String, String> identifierToCurrentFileName = toIdentifierToCurrentFileNameMap(photoFiles);

        List<Renaming> renamings = identifierToPhoto
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().getCounter()))
                .map(entry -> Renaming.builder()
                        .oldName(identifierToCurrentFileName.computeIfAbsent(entry.getKey(), this::throwFileNotFound))
                        .newName(entry.getValue().toFileName())
                        .build()
                )
                .collect(Collectors.toUnmodifiableList());

        List<String> otherIgnoredFiles = identifierToCurrentFileName
                .entrySet()
                .stream()
                .filter(entry -> !identifierToPhoto.containsKey(entry.getKey()))
                .map(Entry::getValue)
                .sorted()
                .collect(Collectors.toUnmodifiableList());

        return Outcome.builder()
                .renamings(renamings)
                .otherIgnoredFiles(otherIgnoredFiles)
                .build();
    }

    private String throwFileNotFound(String identifier) {
        throw new RuntimeException("File to identifier " + identifier + " not found");
    }

    private Map<String, Photo> toIdentifierToPhotoMap(List<Photo> photos) {
        return photos.stream().collect(Collectors.toUnmodifiableMap(
                Photo::getIdentifier,
                Function.identity()
        ));
    }

    private Map<String, String> toIdentifierToCurrentFileNameMap(List<String> photoFiles) {
        return photoFiles.stream()
                .map(filename -> new SimpleImmutableEntry<>(
                        Photo.getIdentifierFromFilename(filename),
                        filename
                ))
                .filter(entry -> entry.getKey().isPresent())
                .collect(Collectors.toMap(
                        entry -> entry.getKey().get(),
                        Entry::getValue
                ));
    }
}
