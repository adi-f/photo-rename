package adfa.photorename.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Outcome {

    List<Renaming> renamings;
    List<String> otherIgnoredFiles;
}
