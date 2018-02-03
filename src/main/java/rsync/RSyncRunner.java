package rsync;

import com.github.fracpete.processoutput4j.output.StreamingProcessOutput;
import com.github.fracpete.rsync4j.RSync;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RSyncRunner {

    public void doRsync() {
        RSync rsync = new RSync()
                .source("/home/robert/Tmp/test/")
                .destination("/home/robert/Tmp/backup/")
                .archive(true)
                .verbose(true)
                .stats(true)
                .delete(true);
        //.compress(true)
        //.humanReadable(true)

        RSyncOutputHandler outputHandler = new RSyncOutputHandler();
        StreamingProcessOutput output = new StreamingProcessOutput(outputHandler);
        try {
            output.monitor(rsync.builder());
            System.out.println("THIS IS RESULT: " + outputHandler.getResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
