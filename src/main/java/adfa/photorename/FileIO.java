package adfa.photorename;

import adfa.photorename.model.Arguments;
import adfa.photorename.model.Renaming;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileIO {

    public InputStream readFile(String path) {
        try {
            return new BufferedInputStream(new FileInputStream(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void printListOfFiles(Arguments arguments) {
        System.out.println("\nFILES OF THIS DIR:");
        listFilesOfCwd(arguments).forEach(System.out::println);
    }

    public List<String> listFilesOfCwd() {
        return listFilesOfCwd((Comparator<File>) null);
    }

    private List<String> listFilesOfCwd(Arguments arguments) {
        if (arguments.getSortFilesBy() == null) {
            return listFilesOfCwd((Comparator<File>) null);
        } else {
            switch (arguments.getSortFilesBy()) {
                case "date-taken":
                    return listFilesOfCwd(new ExifDateTakenComparator());
                default:
                    throw new RuntimeException("Unknown sort by: " + arguments.getSortFilesBy());
            }
        }
    }

    private List<String> listFilesOfCwd(Comparator<File> comparator) {
        Stream<File> files = Stream.of(Objects.requireNonNull(new File(".").listFiles()))
                .filter(File::isFile);
        if (comparator != null) {
            files = files.sorted(comparator);
        }
        return files.map(File::getName)
                .collect(Collectors.toUnmodifiableList());
    }

    public void printRenamings(List<Renaming> renamings) {
        System.out.println("\nRENAMINGS:");
        renamings.forEach(renaming -> System.out.println(renaming.getOldName() + " -> " + renaming.getNewName()));
        System.out.println("Total renaming: " + renamings.size());
    }

    public void printOtherIgnoredFiles(List<String> otherIgnoredFiles) {
        System.out.println("\nOTHER IGNORED FILES:");
        otherIgnoredFiles.forEach(System.out::println);
        System.out.println("Total ignored files: " + otherIgnoredFiles.size());

    }

    public void print(String message) {
        System.out.println(message);
    }

    public void rename(List<Renaming> renamings) {
        renamings.forEach(renaming -> new File(renaming.getOldName()).renameTo(new File(renaming.getNewName())));
    }
}
