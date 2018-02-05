package rsync;

import application.config.Job;
import com.github.fracpete.processoutput4j.core.StreamingProcessOutputType;
import com.github.fracpete.processoutput4j.core.StreamingProcessOwner;
import lombok.extern.slf4j.Slf4j;
import utils.ValueTuple;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class RSyncOutput implements StreamingProcessOwner {
    private boolean firstLinePassed = false;
    private boolean doneCopying = false;
    private boolean done = false;

    private Timestamp startTime;
    RSyncResult.RSyncResultBuilder resultBuilder;

    Pattern numberPattern = Pattern.compile("\\d+(,\\d+)*");

    public RSyncOutput(Job job) {
        resultBuilder = RSyncResult.builder();
        resultBuilder.job(job);
    }

    @Override
    public StreamingProcessOutputType getOutputType() {
        return StreamingProcessOutputType.BOTH;
    }

    @Override
    public void processOutput(String line, boolean stdout) {
        log.debug(line);
        if (stdout) {
            checkIfFirstLine(line);
            if (shouldProcessOutput(line)) {
                processOutput(line);
            }
            checkIfDone(line);
        } else {
            processError(line);
        }
    }

    public RSyncResult getResult() {
        if (!done) {
            log.error("Trying to get result before done. firstLinePassed={}, doneCopying={}, done={}.", firstLinePassed, doneCopying, done);
        }
        return resultBuilder.build();
    }

    private void checkIfFirstLine(String line) {
        if (firstLinePassed) {
            return;
        }
        if (line.contains("sending incremental file list")) {
            startTime = new Timestamp(Calendar.getInstance().getTime().getTime());
            firstLinePassed = true;
        }
    }

    private boolean shouldProcessOutput(String line) {
        if (doneCopying) {
            return true;
        }
        if (line.contains("Number of files:")) {
            doneCopying = true;
            return true;
        }
        return false;
    }

    private void checkIfDone(String line) {
        if (line.contains("total size is") && line.contains("speedup is")) {
            Timestamp endTime = new Timestamp(Calendar.getInstance().getTime().getTime());
            int duration = (int) (endTime.getTime() - startTime.getTime()) / 1000;
            resultBuilder.startTime(startTime);
            resultBuilder.endTime(endTime);
            resultBuilder.duration(duration);
            done = true;
        }
    }

    public void processOutput(String line) {
        if (line.contains("Number of files:")) {
            ValueTuple<Integer, Integer> filesAndFolders = parseFilesAndFolders(numberPattern.matcher(line));
            resultBuilder.files(filesAndFolders.getA());
            resultBuilder.folders(filesAndFolders.getB());
        }
        if (line.contains("Number of created files:")) {
            ValueTuple<Integer, Integer> filesAndFolders = parseFilesAndFolders(numberPattern.matcher(line));
            resultBuilder.filesCreated(filesAndFolders.getA());
            resultBuilder.foldersCreated(filesAndFolders.getB());
        }
        if (line.contains("Number of deleted files:")) {
            ValueTuple<Integer, Integer> filesAndFolders = parseFilesAndFolders(numberPattern.matcher(line));
            resultBuilder.filesDeleted(filesAndFolders.getA());
            resultBuilder.foldersDeleted(filesAndFolders.getB());
        }
        if (line.contains("Total file size:")) {
            long size = parseNumber(numberPattern.matcher(line));
            resultBuilder.size(size);
        }
        if (line.contains("Total transferred file size:")) {
            long transferredSize = parseNumber(numberPattern.matcher(line));
            resultBuilder.transferredSize(transferredSize);
        }
        if (line.contains("sent") && line.contains("received")) {
            int transferSpeed = (int) parseNumber(numberPattern.matcher(line));
            resultBuilder.transferSpeed(transferSpeed);
        }
    }

    private ValueTuple<Integer, Integer> parseFilesAndFolders(Matcher numberMatcher) {
        int files = 0;
        int actualFiles = 0;
        try {
            if (numberMatcher.find()) {
                files = Integer.parseInt(numberMatcher.group());
            }
            if (numberMatcher.find()) {
                actualFiles = Integer.parseInt(numberMatcher.group());
            }
        } catch (NumberFormatException e) {
            log.error("Couldn't parse number from: {}.", numberMatcher.group());
        }
        int folders = files - actualFiles;
        return new ValueTuple<>(actualFiles, folders);
    }

    private long parseNumber(Matcher numberMatcher) {
        long number = 0;
        if (numberMatcher.find()) {
            try {
                String numberString = numberMatcher.group().replace(",","");
                number = Long.parseLong(numberString); //Test with large numbers, may need to remove commas
            } catch (NumberFormatException e) {
                log.error("Couldn't parse number from: {}.", numberMatcher.group());
            }
        }
        return number;
    }

    private void processError(String line) {
        log.error("Problem with rsync: {}.", line);
    }
}