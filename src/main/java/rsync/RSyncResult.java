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
    private long duration;

    private int files;
    private int folders;
    private long size;

    private long transferredSize;
    private int transferSpeed;

    private int filesCreated;
    private int foldersCreated;

    private int filesDeleted;
    private int foldersDeleted;
}
