import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Helper {

    public static String getCurrentIP() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                new URL("http://wtfismyip.com/text").openConnection().getInputStream()
            ));

            StringBuilder contents = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                contents.append(line);
            }

            br.close();

            return contents.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}