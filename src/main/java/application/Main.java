package application;

import rsync.RSyncRunner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Doing stuff now!");
        new RSyncRunner().doRsync();
    }
}
