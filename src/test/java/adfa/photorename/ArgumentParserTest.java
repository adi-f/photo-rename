package adfa.photorename;

import adfa.photorename.model.Arguments;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ArgumentParserTest {

    private ArgumentParser parser = new ArgumentParser();

    private static List<org.junit.jupiter.params.provider.Arguments> testCases() {
        return List.of(
                arguments(
                        ofArgs(),
                        Arguments.builder().dryRun(true).listFilesOnly(false).help(true).build()
                ),
                arguments(
                        ofArgs("-help"),
                        Arguments.builder().dryRun(true).listFilesOnly(false).help(true).build()
                ),
                arguments(
                        ofArgs("-list"),
                        Arguments.builder().dryRun(true).listFilesOnly(true).help(false).build()
                ),

                arguments(
                        ofArgs("-list -sort date-taken"),
                        Arguments.builder().dryRun(true).listFilesOnly(true).sortFilesBy("date-taken").help(false).build()
                ),

                arguments(
                        ofArgs("-csv ./list.csv"),
                        Arguments.builder().csvFilePath("./list.csv").dryRun(true).listFilesOnly(false).help(false).build()
                ),
                arguments(
                        ofArgs("-csv ./list.csv -dry"),
                        Arguments.builder().csvFilePath("./list.csv").dryRun(true).listFilesOnly(false).help(false).build()
                ),
                arguments(
                        ofArgs("-csv ./list.csv -dry -run"),
                        Arguments.builder().csvFilePath("./list.csv").dryRun(true).listFilesOnly(false).help(false).build()
                ),
                arguments(
                        ofArgs("-csv list.csv -run"),
                        Arguments.builder().csvFilePath("list.csv").dryRun(false).listFilesOnly(false).help(false).build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void testParst(String[] args, Arguments expected) {
        assertEquals(expected, parser.parse(args));
    }


    private static String[] ofArgs() {
        return new String[0];
    }
    private static String[] ofArgs(String args) {
        return args.split(" ");
    }

}