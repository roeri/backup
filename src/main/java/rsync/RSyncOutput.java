package rsync;

import com.github.fracpete.processoutput4j.core.StreamingProcessOutputType;
import com.github.fracpete.processoutput4j.core.StreamingProcessOwner;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class RSyncOutput implements StreamingProcessOwner {

    private boolean doneCopying = false;
    RSyncResult.RSyncResultBuilder resultBuilder;
    Pattern numberPattern = Pattern.compile("\\d+");

    public RSyncOutput() {
        resultBuilder = RSyncResult.builder();
    }

    @Override
    public StreamingProcessOutputType getOutputType() {
        return StreamingProcessOutputType.BOTH;
    }

    @Override
    public void processOutput(String line, boolean stdout) {
        if (stdout) {
            if (doneCopying(line)) {
                processOutput(line);
            }
        } else {
            processError(line);
        }
    }

    public RSyncResult getResult() {
        if (!doneCopying) {
            log.error("trying to get result before done");
        }
        return resultBuilder.build();
    }

    private boolean doneCopying(String line) {
        if (doneCopying) {
            return true;
        }
        if (line.contains("Number of files:")) {
            doneCopying = true;
            return true;
        }
        return false;
    }

    public void processOutput(String line) {
        if (line.contains("Number of files:")) {
            parseNumberOfFilesAndFolders(numberPattern.matcher(line));
        }
        if (line.contains("Number of created files:")) {
            parseNumberOfNewFilesAndFolders(numberPattern.matcher(line));
        }
        if (line.contains("Number of deleted files:")) {
            parseNumberOfDeletedFilesAndFolders(numberPattern.matcher(line));
        }
        if (line.contains("Total file size:")) {
            parseTotalFileSize(numberPattern.matcher(line));
        }
        if (line.contains("Total transferred file size:")) {
            parseTransferredSize(numberPattern.matcher(line));
        }
        if (line.contains("sent") && line.contains("received")) {
            parseTransferSpeed(numberPattern.matcher(line));
        }
        System.out.println(line);
    }

    private void parseNumberOfFilesAndFolders(Matcher numberMatcher) {
        int files = 0;
        int actualFiles = 0;
        if (numberMatcher.find()) {
            files = Integer.parseInt(numberMatcher.group());
        }
        if (numberMatcher.find()) {
            actualFiles = Integer.parseInt(numberMatcher.group());
        }
        int folders = files - actualFiles;
        resultBuilder.files(actualFiles);
        resultBuilder.folders(folders);
    }

    private void parseNumberOfNewFilesAndFolders(Matcher numberMatcher) {
        int newFiles = 0;
        int actualNewFiles = 0;
        if (numberMatcher.find()) {
            newFiles = Integer.parseInt(numberMatcher.group());
        }
        if (numberMatcher.find()) {
            actualNewFiles = Integer.parseInt(numberMatcher.group());
        }
        int newFolders = newFiles - actualNewFiles;
        resultBuilder.newFiles(actualNewFiles);
        resultBuilder.newFolders(newFolders);
    }

    private void parseNumberOfDeletedFilesAndFolders(Matcher numberMatcher) {
        int deletedFiles = 0;
        int actualDeletedFiles = 0;
        if (numberMatcher.find()) {
            deletedFiles = Integer.parseInt(numberMatcher.group());
        }
        if (numberMatcher.find()) {
            actualDeletedFiles = Integer.parseInt(numberMatcher.group());
        }
        int deletedFolders = deletedFiles - actualDeletedFiles;
        resultBuilder.deletedFiles(actualDeletedFiles);
        resultBuilder.deletedFolders(deletedFolders);
    }

    private void parseTotalFileSize(Matcher numberMatcher) {
        int size = 0;
        if (numberMatcher.find()) {
            try {
                size = Integer.parseInt(numberMatcher.group()); //Test with large numbers, may need to remove commas
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Couldn't parse size from: " + numberMatcher.group());
            }
        }
        resultBuilder.size(size);
    }

    private void parseTransferredSize(Matcher numberMatcher) {
        int transferredSize = 0;
        if (numberMatcher.find()) {
            try {
                transferredSize = Integer.parseInt(numberMatcher.group()); //Test with large numbers, may need to remove commas
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Couldn't parse transferredSize from: " + numberMatcher.group());
            }
        }
        resultBuilder.transferredSize(transferredSize);
    }

    private void parseTransferSpeed(Matcher numberMatcher) {
        int transferSpeed = 0;
        if (numberMatcher.find() && numberMatcher.find() && numberMatcher.find()) {
            try {
                transferSpeed = Integer.parseInt(numberMatcher.group()); //Test with large numbers, may need to remove commas
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Couldn't parse transferSpeed from: " + numberMatcher.group());
            }
        }
        resultBuilder.transferSpeed(transferSpeed);
    }

    private void processError(String line) {
        log.error("ERROR: " + line);
        System.out.println("ERROR: " + line);
    }
}