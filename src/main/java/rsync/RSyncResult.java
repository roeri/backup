package rsync;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
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

    //ADD START TIME, END TIME, DURATION ETC
}
