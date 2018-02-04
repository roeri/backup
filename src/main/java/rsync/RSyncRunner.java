package rsync;

import application.config.Job;
import com.github.fracpete.processoutput4j.output.StreamingProcessOutput;
import com.github.fracpete.rsync4j.RSync;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RSyncRunner {
    private final String backupRootPath;
    private final List<Job> jobs;

    public RSyncRunner(String backupRootPath, List<Job> jobs) {
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

            RSyncOutputHandler outputHandler = new RSyncOutputHandler();
            StreamingProcessOutput output = new StreamingProcessOutput(outputHandler);
            try {
                output.monitor(rsync.builder());
                System.out.println("THIS IS RESULT FOR JOB '" + job.getName() + "': " + outputHandler.getResult().toString());
            } catch (Exception e) {
                log.error("Failed to run rsync. Exception: " + e.toString());
            }
        }
    }
}
