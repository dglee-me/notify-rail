package kr.co.dglee.notify.delivery.queue;

import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;

public interface DeliveryQueuePublisher {

    void publish(DeliveryRequest request);
}
