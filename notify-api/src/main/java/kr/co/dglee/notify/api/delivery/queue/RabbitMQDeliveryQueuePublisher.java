package kr.co.dglee.notify.api.delivery.queue;

import java.time.LocalDateTime;
import kr.co.dglee.notify.api.config.RabbitMQConfig.RabbitMQProperties;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "notify.queue.type", havingValue = "rabbitmq")
@RequiredArgsConstructor
public class RabbitMQDeliveryQueuePublisher implements DeliveryQueuePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties properties;

    @Override
    public void publish(DeliveryRequest request) {
        rabbitTemplate.convertAndSend(
                properties.deliveryExchange(),
                properties.deliveryRoutingKey(),
                request.getId()
        );

        request.markQueuePublished(LocalDateTime.now());
    }
}
