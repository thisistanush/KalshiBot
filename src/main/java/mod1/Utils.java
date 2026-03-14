package mod1;
import GsonClasses.*;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static PrivateKey loadPrivateKey(String path) throws Exception {
        String pem = Files.readString(Path.of(path));
        pem = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(pem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static String signBase64(PrivateKey privateKey, String message) throws Exception {
        PSSParameterSpec pssSha256 = new PSSParameterSpec(
                "SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1
        );
        Signature sig = Signature.getInstance("RSASSA-PSS");
        sig.setParameter(pssSha256);
        sig.initSign(privateKey);
        sig.update(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(sig.sign());
    }

    public static void clearTextFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
        } catch (IOException e) {
            System.err.println("Error resetting file: " + e.getMessage());
        }
    }

    public static <T> T getHTTP(String url, LinkedHashMap<String, String> headers, Class<T> classType) throws AllErrors.sendHTTPError, IOException {
        Request.Builder builder = new Request.Builder().url(url).get();
        ArrayList<Map.Entry<String, String>> headersList = new ArrayList<>(headers.entrySet());
        for (int i = 0; i < headersList.size(); i++) {
            builder.addHeader(headersList.get(i).getKey(), headersList.get(i).getValue());
        }
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();

            while (response.code() == 429) {
                response.close();
                Thread.sleep(1);
                response = client.newCall(request).execute();
            }

            if (!response.isSuccessful()) {
                throw new AllErrors.sendHTTPError("GET " + url + " failed: HTTP " + response.code());
            }

            String body = response.body().string();
            response.close();
            return gson.fromJson(body, classType);

        } catch (AllErrors.sendHTTPError e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }
}