package devpilot.devpilot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Service
public class AuthService {
    @Value("${github.secret}")
    String GITHUB_SECRET_KEY;
    private static final String HMAC_ALGO = "HmacSHA256";

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
        System.out.println("Secert: "+GITHUB_SECRET_KEY);
        return GITHUB_SECRET_KEY;
    }
}
