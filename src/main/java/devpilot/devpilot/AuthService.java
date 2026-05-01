package devpilot.devpilot;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    static final String GITHUB_HASH_KEY = "GITHUB_HASH_KEY";
    private static final String HMAC_ALGO = "HmacSHA256";

    private Map<String, String> map;
    public AuthService(){
        map = new HashMap<>();
    }

    private static String generateHmac(String data, String secret) throws Exception {

        Mac mac = Mac.getInstance(HMAC_ALGO);

        SecretKeySpec secretKeySpec =
                new SecretKeySpec(secret.getBytes(), HMAC_ALGO);

        mac.init(secretKeySpec);

        byte[] hmacBytes = mac.doFinal(data.getBytes());

        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    public boolean isValid(String hmacSignature, String payload){
        String hashKey = getHashKey();
        try{
            String hmacSign = generateHmac(payload, hashKey);
            System.out.println("Hmac signature: "+hmacSign);
            return hmacSign == hmacSignature;
        }catch (Exception e){
            System.out.println("HMAC Authentication Fail with Reason: "+e.getMessage());
            return false;
        }
    }

    private String getHashKey(){
        if(!map.containsKey(GITHUB_HASH_KEY)){
            String hashKey = getSsmParameter();
            map.put(GITHUB_HASH_KEY, hashKey);
        }
        return map.get(GITHUB_HASH_KEY);
    }

    private String getSsmParameter(){
        return "684759302";
    }
}
