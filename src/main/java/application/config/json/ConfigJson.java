package application.config.json;

import application.config.Config;
import application.config.DbConfig;
import application.config.Job;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;


public class ConfigJson {
    @JsonProperty("database")
    private DbConfigJson dbConfig;

    @JsonProperty("dryRun")
    private boolean dryRun;

    @JsonProperty("compress")
    private boolean compress;

    @JsonProperty("backupRootPath")
    private String backupRootPath;

    @JsonProperty("jobs")
    private List<JobJson> jobs;

    public Config toConfig() {
        DbConfig dBConfig = dbConfig.toDbConfig();
        List<Job> jobs = this.jobs.stream().map(JobJson::toJob).collect(Collectors.toList());
        return new Config(dBConfig, dryRun, compress, backupRootPath, jobs);
    }
}

