package adfa.photorename;

import adfa.photorename.model.Arguments;
import adfa.photorename.model.Outcome;
import adfa.photorename.model.Photo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {

    private final ArgumentParser argumentParser = new ArgumentParser();
    private final FileIO fileIO = new FileIO();
    private final CsvParser csvParser = new CsvParser();
    private final Processor processor = new Processor();

    public static void main(String[] args) {
        new Main().run(args);
    }

    private void run(String[] args) {
        Arguments arguments = argumentParser.parse(args);

        if(arguments.isHelp()) {
            fileIO.print(argumentParser.getDescription());
            return;
        }

        if(arguments.isListFilesOnly()) {
            fileIO.printListOfFiles(arguments);
            return;
        }

        List<Photo> photos;
        try (InputStream is = fileIO.readFile(arguments.getCsvFilePath())) {
            photos = csvParser.parse(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> photoFiles = fileIO.listFilesOfCwd();
        Outcome outcome = processor.process(photos, photoFiles);

        fileIO.printRenamings(outcome.getRenamings());
        fileIO.printOtherIgnoredFiles(outcome.getOtherIgnoredFiles());

        if (arguments.isDryRun()) {
            fileIO.print("Skipping renaming, done.");
        } else {
            fileIO.rename(outcome.getRenamings());
            fileIO.print("All DONE!");
        }
    }
}
