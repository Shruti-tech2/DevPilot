package devpilot.devpilot.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import devpilot.devpilot.AuthService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController

public class WebhookController {
    static final String SIGNATURE = "X-Hub-Signature-256";
    AuthService authService;
    ObjectMapper mapper;
    WebhookController(){
        authService = new AuthService();
        mapper = new ObjectMapper();
    }
    @PostMapping("/devpilot/webhook_listener")
    public ResponseEntity<Object> webhookListener(@RequestBody String request,
                                                  @RequestHeader Map<String, String> headers)
    {
        try {
            if(headers == null || !headers.containsKey(SIGNATURE)){
                return ResponseEntity.badRequest().body("Authentication signature is required");
            }
            String hmacSign = headers.get(SIGNATURE);
            if(!authService.isValid(hmacSign, request)){
                return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
            }

            GitHubPushEvent payload =
                    mapper.readValue(request, GitHubPushEvent.class);
            System.out.println("Request: "+request);

            return ResponseEntity.accepted().body("Webhook accepted github");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
