package application.config.json;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DBConfig {
    private String host;
    private String name;
    private String user;
    private String pass;

    public static final DBConfig EMPTY = new DBConfig("", "", "", "");
}
