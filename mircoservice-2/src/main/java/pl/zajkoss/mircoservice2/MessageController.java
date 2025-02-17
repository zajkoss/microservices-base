package pl.zajkoss.mircoservice2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/msg")
@RefreshScope
public class MessageController {

    @Value("${message_to_display:Message not found}")
    private String messageToDisplay;

    @Value("${message_current_branch}")
    private String messageCurrentBranch;

    @GetMapping("")
    public String get() {
        return messageToDisplay + "\n branch:" + messageCurrentBranch;
    }
}
