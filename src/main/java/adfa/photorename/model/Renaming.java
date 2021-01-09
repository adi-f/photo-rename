package adfa.photorename.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Renaming {
    String oldName;
    String newName;
}
