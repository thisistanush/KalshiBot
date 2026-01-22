import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        kalshiAPI.publicGet(
                "https://api.elections.kalshi.com/trade-api/v2/markets" +
                        "?limit=100" +
                        "&cursor=");

        String line2 = "";
        while(!"null".equals(line2)){
            BufferedReader reader = Files.newBufferedReader(Path.of("/Users/tanushmusham/IDEA/projects/kalshiBOT/src/main/resources/json.txt"));
            String line = reader.readLine();
            int num = line.indexOf("cursor");
            int num2 = line.indexOf(",");
            line2 = line.substring(num + 9, num2 - 1);

            System.in.read();

            kalshiAPI.publicGet(
                    "https://api.elections.kalshi.com/trade-api/v2/markets" +
                            "?limit=100" +
                            "&cursor=" +
                    line2);

        }




    }


}

