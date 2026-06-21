package kr.co.dglee.notify.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan(basePackages = "kr.co.dglee.notify.domain")
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class NotifyWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifyWorkerApplication.class, args);
    }
}