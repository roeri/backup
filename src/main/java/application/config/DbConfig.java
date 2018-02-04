package application.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DbConfig {
    private String hostname;
    private String database;
    private String username;
    private String password;
}
