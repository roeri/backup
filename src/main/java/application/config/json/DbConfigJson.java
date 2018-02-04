package application.config.json;

import application.config.DbConfig;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DbConfigJson {
    @JsonProperty("hostname")
    private String hostname;

    @JsonProperty("database")
    private String database;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    public DbConfig toDbConfig() {
        return new DbConfig(hostname, database, username, password);
    }

}
