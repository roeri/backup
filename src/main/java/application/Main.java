package application;

import application.config.Config;
import application.config.json.ConfigJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class Main {
    private static Application application;

    public static void main(String[] args) {
        Config config = readConfigFile();
        application = new Application(config);
    }

    private static Config readConfigFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Config config = objectMapper.readValue(new File(System.getProperty("user.home") + "/.config/backup/backup.conf"), ConfigJson.class).toConfig();
            log.debug("Config read successfully.");
            return config;
        } catch (IOException e) {
            log.error("Couldn't read config file, exiting. Exception: {}.", e.toString());
            System.exit(1);
            return null;
        }
    }
}
