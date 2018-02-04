package application.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static java.util.Collections.*;

@Getter
@AllArgsConstructor
public class Config {
    private String backupRootPath;
    private List<Job> jobs;

    public static final Config EMPTY = new Config("", emptyList());
}
