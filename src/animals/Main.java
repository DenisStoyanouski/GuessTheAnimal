package animals;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    static String fileType;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            fileType = "json";
        } else {
            fileType = args[1];
        }

        Play app = new Play();
        app.launchFile();
        app.gameStart();

    }
}