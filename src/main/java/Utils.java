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
import java.util.Base64;

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

    public static void clearJSONfile(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/tanushmusham/IDEA/projects/kalshiBOT/src/main/resources/json.txt", false))) {
            // No need to write anything; opening it in this mode truncates the file to 0 bytes
        } catch (IOException e) {
            System.err.println("Error resetting file: " + e.getMessage());
        }
    }






}
