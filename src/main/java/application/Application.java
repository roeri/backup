package application;

import application.config.Config;
import lombok.extern.slf4j.Slf4j;
import rsync.RSyncRunner;

@Slf4j
public class Application {
    private final Config config;

    public Application(Config config) {
        log.info("Application started.");
        this.config = config;
        RSyncRunner rSyncRunner = new RSyncRunner(config);
        rSyncRunner.runRsync();
    }
}
