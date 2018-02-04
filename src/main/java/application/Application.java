package application;

import application.config.Config;
import rsync.RSyncRunner;

public class Application {
    private final Config config;

    public Application(Config config) {
        this.config = config;
        RSyncRunner rSyncRunner = new RSyncRunner(config.isDryRun(), config.isCompress(), config.getDBConfig(), config.getBackupRootPath(), config.getJobs());
        rSyncRunner.doRsync();
    }
}
