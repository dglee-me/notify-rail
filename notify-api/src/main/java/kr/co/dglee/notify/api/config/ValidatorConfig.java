package kr.co.dglee.notify.api.config;

import kr.co.dglee.notify.domain.delivery.validation.DeliveryTargetValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfig {

    @Bean
    public DeliveryTargetValidator deliveryTargetValidator() {
        return new DeliveryTargetValidator();
    }
}
