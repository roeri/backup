package application;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {
    @JsonProperty("backupRootPath")
    private String backupRootPath;
}
