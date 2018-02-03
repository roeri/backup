package rsync;

import com.github.fracpete.processoutput4j.core.StreamingProcessOutputType;
import com.github.fracpete.processoutput4j.core.StreamingProcessOwner;

public class RsyncOutputHandler implements StreamingProcessOwner {
    public StreamingProcessOutputType getOutputType() {
        return StreamingProcessOutputType.BOTH; //TODO: Make one processor of each type?
    }
    public void processOutput(String line, boolean stdout) {
        System.out.println((stdout ? "[OUT] " : "[ERR] ") + line);
    }
}