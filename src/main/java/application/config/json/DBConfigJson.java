package application.config.json;

import application.config.DBConfig;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DBConfigJson {
    @JsonProperty("hostname")
    private String hostname;

    @JsonProperty("database")
    private String database;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    public DBConfig toDBConfig() {
        return new DBConfig(hostname, database, username, password);
    }

}
