package adfa.photorename;

import adfa.photorename.model.Renaming;

import java.io.*;
import java.util.List;
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

    public List<String> listFilesOfCwd() {
        return Stream.of(new File(".").listFiles())
                .filter(File::isFile)
                .map(File::getName)
                .collect(Collectors.toUnmodifiableList());
    }

    public void printRenamings(List<Renaming> renamings) {
        System.out.println("\nRENAMINGS:");
        renamings.forEach(renaming -> System.out.println(renaming.getOldName() + " -> " + renaming.getNewName()));
        System.out.println("Total renaming: " + renamings.size());
    }

    public void printOtherIgnoredFiles(List<String> otherIgnoredFiles) {
        System.out.println("\nOTEHR IGNORED FILES:");
        otherIgnoredFiles.forEach(System.out::println);
        System.out.println("Total ignored files: " + otherIgnoredFiles.size());

    }

    public void printDone() {
        System.out.println("All DONE!");
    }

    public void printDryRun() {
        System.out.println("Skipping renaming, done.");
    }

    public void rename(List<Renaming> renamings) {
        renamings.forEach(renaming -> new File(renaming.getOldName()).renameTo(new File(renaming.getNewName())));
    }
}
