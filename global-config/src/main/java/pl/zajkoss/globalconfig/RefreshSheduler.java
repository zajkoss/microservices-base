package pl.zajkoss.globalconfig;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshSheduler {

    private final RefreshConfigService refreshConfigService;

    public RefreshSheduler(RefreshConfigService refreshConfigService) {
        this.refreshConfigService = refreshConfigService;
    }

    @Scheduled(fixedRate = 50000)
    public void refresh() {
        System.out.println("Start checking new config...");
        if(refreshConfigService.isNewConfigVersionAvailable()) {
            System.out.println("New config version available");
            refreshConfigService.refreshConfigs();
        }
    }

}
