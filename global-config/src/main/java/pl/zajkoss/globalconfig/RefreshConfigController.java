package pl.zajkoss.globalconfig;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RefreshConfigController {

    private final RestClient restClient;

    public RefreshConfigController(RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refreshConfig() {
        String eurekaResponse = restClient.get()
                .uri("http://localhost:8761/eureka/apps")
                .header("Accept", "application/json")
                .retrieve()
                .body(String.class);

        List<String> homePageUrls = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(eurekaResponse);
            JsonNode applications = root.path("applications").path("application");
            if (applications.isArray()) {
                for (JsonNode app : applications) {
                    for (JsonNode instance : app.path("instance")) {
                        homePageUrls.add(instance.path("homePageUrl").asText());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        //Call all refresh's for all services
        homePageUrls.stream()
                .forEach(homePageUrl -> {
                    System.out.println("Try to refresh: " + homePageUrl);
                    String refreshedResponde = "Service wasn't refreshed successfully";
                    try {
                        restClient.post()
                                .uri(homePageUrl + "/actuator/refresh")
                                .retrieve()
                                .body(String.class);
                        refreshedResponde = "Service was refreshed successfully";
                    } catch(RestClientResponseException e) {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                            // Ignore, because actuator wan not implemented for this service
                        };
                    }
                    System.out.println("Refreshed result: " + refreshedResponde);
                });

        return ResponseEntity.ok("Refreshed config");
    }
}
