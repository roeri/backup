package application.config.json;

import application.config.Job;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobJson {
    @JsonProperty("name")
    private String name;

    @JsonProperty("sourcePath")
    private String sourcePath;

    public Job toJob() {
        return new Job(name, sourcePath);
    }
}
