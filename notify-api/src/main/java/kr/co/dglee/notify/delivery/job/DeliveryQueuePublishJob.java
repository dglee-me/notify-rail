package kr.co.dglee.notify.delivery.job;

import kr.co.dglee.notify.delivery.queue.DeliveryQueuePublisher;
import kr.co.dglee.notify.delivery.repository.DeliveryRequestRepository;
import kr.co.dglee.notify.domain.delivery.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeliveryQueuePublishJob {

    private final DeliveryRequestRepository deliveryRequestRepository;
    private final DeliveryQueuePublisher deliveryQueuePublisher;

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public void publishPendingRequests() {
        deliveryRequestRepository
                .findByStatusOrderByCreatedAtAsc(DeliveryStatus.PENDING)
                .forEach(deliveryQueuePublisher::publish);
    }
}
