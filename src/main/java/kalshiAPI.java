import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.PrivateKey;

public class kalshiAPI {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();


    public static void publicGet(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<InputStream> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
        readAll(response.body());

        System.out.println("HTTP " + response.statusCode());
    }

//    public static void getOrderBook(String ticker) throws Exception {
//        PrivateKey key = Utils.loadPrivateKey("/Users/tanushmusham/Documents/API_keys/Kalshi/kalshi_private_key_pkcs8.pem");
//
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        String path = "/trade-api/v2/markets/" + ticker + "/orderbook";
//        String message = timestamp + "GET" + path;
//
//        String signature = Utils.signBase64(key, message);
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(BASE_TRADE + path))
//                .header("Accept", "application/json")
//                .header("KALSHI-ACCESS-KEY", "1c8e69b1-79a2-48ba-8940-66dd2c1082cd")
//                .header("KALSHI-ACCESS-TIMESTAMP", timestamp)
//                .header("KALSHI-ACCESS-SIGNATURE", signature)
//                .GET()
//                .build();
//
//        HttpResponse<InputStream> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
//        String body = readAll(response.body());
//
//        System.out.println("HTTP " + response.statusCode());
//        System.out.println(body);
//    }

    private static void readAll(InputStream in) throws Exception {
        File file = new File("/Users/tanushmusham/IDEA/projects/kalshiBOT/src/main/resources/json.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
             // 'false' ensures the file is cleared before writing
             FileWriter fw = new FileWriter(file, false)) {

            String line;
            while ((line = br.readLine()) != null) {
                fw.write(line);
                fw.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
