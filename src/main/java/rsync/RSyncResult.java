package rsync;

import application.config.Job;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;

@ToString
@Builder
@Getter
public class RSyncResult {
    private Job job;

    private Timestamp startTime;
    private Timestamp endTime;
    private int duration;

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
