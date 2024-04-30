package crm.crm_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrmServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmServiceApplication.class, args);
    }

}
