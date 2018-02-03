package application;

import rsync.RSyncRunner;

public class Main {

    public static void main(String[] args) {
        new RSyncRunner().doRsync();
    }
}
