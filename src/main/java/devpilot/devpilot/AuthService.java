package devpilot.devpilot;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    @Value("${github.secret}")
    String GITHUB_SECRET_KEY;
    private static final String HMAC_ALGO = "HmacSHA256";

    private Map<String, String> map;
    public AuthService(){
        map = new HashMap<>();
    }

    private static String generateHmac(String data, String secret) throws Exception {

        Mac mac = Mac.getInstance(HMAC_ALGO);

        SecretKeySpec secretKeySpec =
                new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);

        mac.init(secretKeySpec);

        byte[] hmacBytes =
                mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();

        for (byte b : hmacBytes) {
            hexString.append(String.format("%02x", b));
        }

        return "sha256=" + hexString;
    }

    public boolean isValid(String hmacSignature, String payload){
        String hashKey = getHashKey();
        try{
            String hmacSign = generateHmac(payload, hashKey);
            return hmacSign.equals(hmacSignature);
        }catch (Exception e){
            System.out.println("HMAC Authentication Fail with Reason: "+e.getMessage());
            return false;
        }
    }

    private String getHashKey(){
        return GITHUB_SECRET_KEY;
    }
}
