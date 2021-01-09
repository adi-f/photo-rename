package adfa.photorename;

import adfa.photorename.model.Arguments;
import adfa.photorename.model.Outcome;
import adfa.photorename.model.Photo;

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
        List<Photo> photos = csvParser.parse(fileIO.readFile(arguments.getCsvFilePath()));
        List<String> photoFiles = fileIO.listFilesOfCwd();
        Outcome outcome = processor.process(photos, photoFiles);

        fileIO.printRenamings(outcome.getRenamings());
        fileIO.printOtherIgnoredFiles(outcome.getOtherIgnoredFiles());

        if (arguments.isDryRun()) {
            fileIO.rename(outcome.getRenamings());
            fileIO.printDone();
        } else {
            fileIO.printDryRun();
        }
    }
}
