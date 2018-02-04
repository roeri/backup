package rsync;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class RSyncResult {
    private int files;
    private int folders;
    private int size;

    private int newFiles;
    private int newFolders;
    private int transferredSize;
    private int transferSpeed;

    private int deletedFiles;
    private int deletedFolders;
}
