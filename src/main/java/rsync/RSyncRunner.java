package rsync;

import application.config.DBConfig;
import application.config.Job;
import com.github.fracpete.processoutput4j.output.StreamingProcessOutput;
import com.github.fracpete.rsync4j.RSync;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class RSyncRunner {
    private final boolean dryRun;
    private final boolean compress;
    private final DBConfig dbConfig;
    private final String backupRootPath;
    private final List<Job> jobs;

    public RSyncRunner(boolean dryRun, boolean compress, DBConfig dbConfig, String backupRootPath, List<Job> jobs) {
        this.dryRun = dryRun;
        this.compress = compress;
        this.dbConfig = dbConfig;
        this.backupRootPath = backupRootPath;
        this.jobs = jobs;
    }

    public void doRsync() {
        for (Job job : jobs) {
            log.debug("Starting job {}.", job.getName());
            RSync rsync = new RSync()
                    .source(job.getSourcePath())
                    .destination(backupRootPath)
                    .archive(true)
                    .verbose(true)
                    .stats(true)
                    .delete(true)
                    .compress(compress)
                    .listOnly(dryRun); //TODO: Add .compress(boolean) as a configurable option?

            RSyncOutput outputHandler = new RSyncOutput();
            StreamingProcessOutput output = new StreamingProcessOutput(outputHandler);
            try {
                Timestamp startTime = new Timestamp(Calendar.getInstance().getTime().getTime());
                output.monitor(rsync.builder()); //TODO: IMPORTANT! May need to do some wait polling?!
                Timestamp endTime = new Timestamp(Calendar.getInstance().getTime().getTime());
                int duration = (int) (endTime.getTime() - startTime.getTime()) / 1000;
                logResults(job, startTime, endTime, duration, outputHandler.getResult());
            } catch (Exception e) {
                log.error("Failed to run rsync. Exception: " + e.toString());
            }
            log.debug("Finished job {}.", job.getName());
        }
    }

    private void logResults(Job job, Timestamp startTime, Timestamp endTime, int duration, RSyncResult result) {
        String url = "jdbc:mysql://" + dbConfig.getHostname() + "/" + dbConfig.getDatabase() + "?useSSL=false";
        try {
            Connection conn = DriverManager.getConnection(url, dbConfig.getUsername(), dbConfig.getPassword());
            Statement st = conn.createStatement();
            String sql = createInsertSql(job, startTime, endTime, duration, result);
            st.executeUpdate(sql);
            conn.close();
        } catch (SQLException e) {
            log.error("SQL error: " + e.getSQLState());
        }
    }

    private String createInsertSql(Job job, Timestamp startTime, Timestamp endTime, int duration, RSyncResult result) {
        return String.format("insert into backups values('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                job.getName(), startTime, endTime, duration, result.getTransferredSize(), result.getSize(), result.getTransferSpeed(),
                result.getNewFiles(), result.getDeletedFiles(), job.getSourcePath(), backupRootPath);
    }
}
