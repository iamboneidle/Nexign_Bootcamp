package cdr.cdr_service;

import cdr.cdr_service.Services.MsisdnsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CdrServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CdrServiceApplication.class, args);
    }
}
