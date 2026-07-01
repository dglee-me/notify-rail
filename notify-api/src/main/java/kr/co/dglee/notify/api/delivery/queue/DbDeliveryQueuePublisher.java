package kr.co.dglee.notify.api.delivery.queue;

import java.time.LocalDateTime;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "notify.queue.type", havingValue = "db", matchIfMissing = true)
public class DbDeliveryQueuePublisher implements DeliveryQueuePublisher {
    @Override
    public void publish(DeliveryRequest request) {
        request.markQueuePublished(LocalDateTime.now());
    }
}
