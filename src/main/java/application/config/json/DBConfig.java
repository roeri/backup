package application.config.json;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DBConfig {
    private String hostname;
    private String database;
    private String username;
    private String password;
}
