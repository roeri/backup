package application.config.json;

import application.config.Job;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DBConfigJson {
    @JsonProperty("host")
    private String host;

    @JsonProperty("name")
    private String name;

    @JsonProperty("user")
    private String user;

    @JsonProperty("pass")
    private String pass;

    public DBConfig toDBConfig() {
        return new DBConfig(host, name, user, pass);
    }
}
