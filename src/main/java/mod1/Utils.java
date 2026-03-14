package java;
//import package name if different package

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;

public class Utils {
    private static final PSSParameterSpec PSS_SHA256 = new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1);

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
        Signature sig = Signature.getInstance("RSASSA-PSS");
        sig.setParameter(PSS_SHA256);
        sig.initSign(privateKey);
        sig.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = sig.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    public static void clearTextFile(String filePath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
        } catch (IOException e) {
            System.err.println("Error resetting file: " + e.getMessage());
        }
    }

    public static void getHTTPS(String url, LinkedHashMap<String, String> headers, String whichClass) throws AllErrors.getHTTPSError, IOException{
        try {
            OkHttpClient client = new OkHttpClient();

            Request.Builder builder = new Request.Builder().url(url).get();
            var headersList = new ArrayList<>(headers.entrySet());
            for(int i = 0; i < headersList.size(); i++){
                builder.addHeader(headersList.get(i).getValue(), headersList.get(i).getKey());
            }
            Request request = builder.build();

            Response response = client.newCall(request).execute();
            String json = response.body().string();
            System.out.println("HTTP code: " + response.code());

            Gson gson = new Gson();

            switch(whichClass){
                case "java.MarketResponse":
                    MarketResponse marketResponse = gson.fromJson(json, MarketResponse.class);
                    System.out.println("java.MarketResponse class updated");
                    break;
                //add more for post put and delete (add exception too)

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            throw AllErrors.getHTTPSError;
        }


    }






}
