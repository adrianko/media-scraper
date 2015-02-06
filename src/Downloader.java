import java.io.IOException;

public class Downloader {
    
    public static void enqueue(String magnet) {
        try {
            Runtime.getRuntime().exec("cmd /c start " + magnet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}