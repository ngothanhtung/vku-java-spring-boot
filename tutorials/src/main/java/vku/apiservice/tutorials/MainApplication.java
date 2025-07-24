package vku.apiservice.tutorials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import vku.apiservice.tutorials.config.JwtConfigProperties;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(JwtConfigProperties.class)
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
