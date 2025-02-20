package pl.zajkoss.globalconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigServer
@EnableScheduling
public class GlobalConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlobalConfigApplication.class, args);
    }

}
