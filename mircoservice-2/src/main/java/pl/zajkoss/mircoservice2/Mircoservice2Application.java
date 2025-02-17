package pl.zajkoss.mircoservice2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Mircoservice2Application {

    public static void main(String[] args) {
        SpringApplication.run(Mircoservice2Application.class, args);
    }

}
