package rsync;

import com.github.fracpete.processoutput4j.output.StreamingProcessOutput;
import com.github.fracpete.rsync4j.RSync;

public class RSyncRunner {

    public void doRsync() {
        RSync rsync = new RSync()
                .source("/home/robert/Tmp/test/")
                .destination("/home/robert/Tmp/backup/")
                .archive(true)
                .verbose(true);
        //.compress(true)
        //.humanReadable(true)

        StreamingProcessOutput output = new StreamingProcessOutput(new RsyncOutputHandler());
        try {
            output.monitor(rsync.builder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
