package pl.zajkoss.globalconfig;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RefreshConfigController {

    private final RestClient restClient;
    private final RefreshConfigService refreshConfigService;

    public RefreshConfigController(RestClient restClient, RefreshConfigService refreshConfigService) {
        this.restClient = restClient;
        this.refreshConfigService = refreshConfigService;
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refreshConfig() {
        return ResponseEntity.ok(refreshConfigService.refreshConfigs());
    }

    @PostMapping("/refresh-post")
    public ResponseEntity<String> refreshConfigPost() {
        return ResponseEntity.ok(refreshConfigService.refreshConfigs());
    }
}
