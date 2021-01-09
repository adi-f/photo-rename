package adfa.photorename;

import adfa.photorename.model.Arguments;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

    public Arguments parse(String[] args) {
        Map<String, String> arguments = parseArgs(args);

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
            argumets.put(paramValue, paramName);
        }
        return argumets;
    }
}
