package pl.zajkoss.globalconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
public class RefreshConfigService {

    @Value("${config.client.version:-1}")
    private String configVersion;

    private final RestClient restClient;

    public RefreshConfigService(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean isNewConfigVersionAvailable() {
        String configFromRepo = restClient.get()
                .uri("http://localhost:8888/actuator/dev")
                .header("Accept", "application/json")
                .retrieve()
                .body(String.class);
        String newVersion = "-1";
        try {;
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(configFromRepo);
            newVersion = root.path("version").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Check new version of config " + configVersion + " and " + newVersion);
        boolean result = !configVersion.equals(newVersion);
        configVersion = newVersion;
        return result;
    }

    public String refreshConfigs() {
        List<String> homePageUrls = getMicroservicesAddresses();

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
                    } catch (RestClientResponseException e) {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                            // Ignore, because actuator wan not implemented for this service
                        }
                        ;
                    }
                    System.out.println("Refreshed result: " + refreshedResponde);
                });

        return "Refreshed config";
    }

    private List<String> getMicroservicesAddresses() {
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
        return homePageUrls;
    }
}
