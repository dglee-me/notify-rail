package kr.co.dglee.notify.api.delivery.queue;

import java.time.LocalDateTime;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import org.springframework.stereotype.Component;

@Component
public class DbDeliveryQueuePublisher implements DeliveryQueuePublisher {
    @Override
    public void publish(DeliveryRequest request) {
        request.markQueuePublished(LocalDateTime.now());
    }
}
