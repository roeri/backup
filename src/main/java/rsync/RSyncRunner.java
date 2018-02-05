package rsync;

import application.config.Config;
import application.config.DbConfig;
import application.config.Job;
import com.github.fracpete.processoutput4j.output.StreamingProcessOutput;
import com.github.fracpete.rsync4j.RSync;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RSyncRunner {
    private final Config config;

    public RSyncRunner(Config config) {
        this.config = config;
    }

    public void runRsync() {
        List<RSyncResult> results = new ArrayList<>();
        for (Job job : config.getJobs()) {
            log.info("Starting job {}.", job.getName());
            RSync rsync = new RSync()
                    .source(job.getSourcePath())
                    .destination(config.getBackupRootPath())
                    .archive(true)
                    .verbose(true)
                    .stats(true)
                    .delete(true)
                    .compress(config.isCompress())
                    .listOnly(config.isDryRun());
            try {
                RSyncOutput outputHandler = new RSyncOutput(job);
                StreamingProcessOutput output = new StreamingProcessOutput(outputHandler);
                output.monitor(rsync.builder()); //TODO: IMPORTANT! May need to do some wait polling?!
                results.add(outputHandler.getResult());
            } catch (Exception e) {
                log.error("Failed to run rsync. Exception: {}.", e.toString());
            }
            log.info("Finished job {}.", job.getName());
        }
        try {
            logResults(results);
        } catch (SQLException e) {
            log.error("SQL error: {}.", e.getSQLState());
        }
    }

    private void logResults(List<RSyncResult> results) throws SQLException {
        log.info("Logging results to database.");
        DbConfig dbConfig = config.getDbConfig();
        String url = "jdbc:mysql://" + dbConfig.getHostname() + "/" + dbConfig.getDatabase() + "?useSSL=false";
        Connection conn = DriverManager.getConnection(url, dbConfig.getUsername(), dbConfig.getPassword());

        for (RSyncResult result : results) {
            Statement st = conn.createStatement();
            String sql = createInsertSql(result);
            st.executeUpdate(sql);
        }
        conn.close();
        log.info("Done logging results to database.");
    }

    private String createInsertSql(RSyncResult result) {
        return String.format("insert into backups (jobName, startTime, endTime, duration, files, folders, " +
                        "size, transferredSize, transferSpeed, filesCreated, foldersCreated, filesDeleted, " +
                        "foldersDeleted, sourcePath, targetPath) values('%s', '%s', '%s', '%s', '%s', '%s', '%s', " +
                        "'%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                result.getJob().getName(), result.getStartTime(), result.getEndTime(), result.getDuration(),
                result.getFiles(), result.getFolders(), result.getSize(), result.getTransferredSize(),
                result.getTransferSpeed(), result.getFilesCreated(), result.getFoldersCreated(), result.getFilesDeleted(),
                result.getFoldersDeleted(), result.getJob().getSourcePath(), config.getBackupRootPath());
    }
}
