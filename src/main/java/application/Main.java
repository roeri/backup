package application;

import com.fasterxml.jackson.databind.ObjectMapper;
import rsync.RSyncRunner;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Doing stuff now!");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Config config = objectMapper.readValue(new File("/home/robert/Src/git/backup/src/main/resources/config_example.json"), Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new RSyncRunner().doRsync();
    }
}
