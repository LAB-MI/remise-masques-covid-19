package fr.gouv.interieur.dmgp.remettant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.ZoneId;

@SpringBootApplication
public class RemettantApplication {
    private static final String ZONE_UTC = "UTC";

    public static void main(String[] args) {
        SpringApplication.run(RemettantApplication.class, args);
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of(ZONE_UTC));
    }
}
