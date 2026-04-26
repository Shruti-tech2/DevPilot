package devpilot.devpilot.webhook;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {
    @PostMapping("/devpilot/webhook_listener")
    public ResponseEntity<Object> webhookListener(@RequestBody GitHubPushEvent request)
    {
        System.out.println(request);
        return ResponseEntity.accepted().body("Webhook accepted github");
    }
}
