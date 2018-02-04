package application.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Config {
    private DbConfig dbConfig;
    private boolean dryRun;
    private boolean compress;
    private String backupRootPath;
    private List<Job> jobs;
}
