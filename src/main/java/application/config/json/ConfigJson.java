package application.config.json;

import application.config.Config;
import application.config.Job;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;


public class ConfigJson {
    @JsonProperty("backupRootPath")
    private String backupRootPath;

    @JsonProperty("jobs")
    private List<JobJson> jobs;

    public Config toConfig() {
        List<Job> jobs = this.jobs.stream().map(JobJson::toJob).collect(Collectors.toList());
        return new Config(backupRootPath, jobs);
    }
}

