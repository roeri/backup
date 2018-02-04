package application;

import application.config.Config;
import application.config.json.ConfigJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import rsync.RSyncRunner;

import java.io.File;
import java.io.IOException;

@Slf4j
public class Main {
    private static Config config = Config.EMPTY;

    public static void main(String[] args) {
        readConfigFile();
        new RSyncRunner(config.getBackupRootPath(), config.getJobs()).doRsync();
    }

    private static void readConfigFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ConfigJson configJson = objectMapper.readValue(new File("/home/robert/Src/git/backup/src/main/resources/config_example.json"), ConfigJson.class);
            config = configJson.toConfig();
        } catch (IOException e) {
            log.error("Couldn't read config file. Exception: " + e.toString());
        }
    }
}
