package rsync;

import application.config.Job;
import application.config.json.DBConfig;
import com.github.fracpete.processoutput4j.output.StreamingProcessOutput;
import com.github.fracpete.rsync4j.RSync;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class RSyncRunner {
    private final DBConfig dbConfig;
    private final String backupRootPath;
    private final List<Job> jobs;

    public RSyncRunner(DBConfig dbConfig, String backupRootPath, List<Job> jobs) {
        this.dbConfig = dbConfig;
        this.backupRootPath = backupRootPath;
        this.jobs = jobs;
    }

    public void doRsync() {
        for (Job job : jobs) {
            RSync rsync = new RSync()
                    .source(job.getSourcePath())
                    .destination(backupRootPath)
                    .archive(true)
                    .verbose(true)
                    .stats(true)
                    .delete(true); //TODO: Add .compress(boolean) as a configurable option?

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
                //TODO: logFailure(job.getName));
            }
        }
    }

    private void logResults(Job job, Timestamp startTime, Timestamp endTime, int duration, RSyncResult result) {
        String url = "jdbc:mysql://" + dbConfig.getHost() + "/" + dbConfig.getName();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, dbConfig.getUser(), dbConfig.getPass());
            Statement st = conn.createStatement();
            String sql = String.format("insert into backups values('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                    job.getName(), startTime, endTime, duration, result.getTransferredSize(), result.getSize(), result.getTransferSpeed(),
                    result.getNewFiles(), result.getDeletedFiles(), job.getSourcePath(), backupRootPath);
            st.executeUpdate(sql);
            //"insert into simpletest values('" + job + "')"
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
