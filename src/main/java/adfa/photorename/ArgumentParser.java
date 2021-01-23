package adfa.photorename;

import adfa.photorename.model.Arguments;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ArgumentParser {

    public String getDescription() {
        return "Arguments:\n" +
                "-help (or no args): print this help\n" +
                "-list:              list all files of this dir\n" +
                "-sort <by>:         sort by:\n" +
                "                    \"date-taken\": sort ascending by date take (EXIF)\n" +
                "-csv <path> -dry:   print output, but do nothing\n" +
                "-csv <path> -run:   print output and RENAME the files";
    }

    public Arguments parse(String[] args) {
        Map<String, String> arguments = parseArgs(args);

        boolean help = arguments.isEmpty() || arguments.containsKey("-help");
        if(help) {
            return Arguments.builder().help(true).dryRun(true).build();
        }

        boolean listFilesOnly = Set.of("-list").equals(arguments.keySet())
                || Set.of("-list", "-sort").equals(arguments.keySet());
        if(listFilesOnly) {
            return Arguments.builder().listFilesOnly(true).dryRun(true).sortFilesBy(arguments.get("-sort")).build();
        }

        String csvFilePath = arguments.get("-csv");
        boolean dryRun = arguments.containsKey("-dry") || !arguments.containsKey("-run");
        return Arguments.builder()
                .csvFilePath(csvFilePath)
                .dryRun(dryRun)
                .build();
    }

    private Map<String, String> parseArgs(String[] args) {
        Map<String, String> argumets = new HashMap<>();

        for(int i = 0; i < args.length; i++) {
            String paramName = args[i];
            String paramValue;
            if(!paramName.startsWith("-")) {
                throw new RuntimeException("Can't read arguments!");
            }
            if(i < args.length-1 && !args[i+1].startsWith("-")) {
                paramValue = args[i+1];
                i++;
            } else {
                paramValue = null;
            }
            argumets.put(paramName, paramValue);
        }
        return argumets;
    }
}
