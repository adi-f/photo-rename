package adfa.photorename.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Arguments {
    String csvFilePath;
    boolean dryRun;
    boolean listFilesOnly;
    boolean help;
}
