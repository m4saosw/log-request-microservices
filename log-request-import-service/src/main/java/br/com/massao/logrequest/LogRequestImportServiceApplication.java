package br.com.massao.logrequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class LogRequestImportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogRequestImportServiceApplication.class, args);
    }

}
