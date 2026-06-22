package kr.co.dglee.notify.api.delivery.job;

import java.time.LocalDateTime;
import java.util.List;
import kr.co.dglee.notify.api.delivery.queue.DeliveryQueuePublisher;
import kr.co.dglee.notify.api.delivery.repository.DeliveryRequestRepository;
import kr.co.dglee.notify.domain.delivery.DeliveryStatus;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryQueuePublishJob {

    private final DeliveryRequestRepository deliveryRequestRepository;
    private final DeliveryQueuePublisher deliveryQueuePublisher;

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public void publish() {
        publishPendingRequests();
        publishRetryRequests();
    }

    /**
     * Pending 상태의 Delivery Request Publish
     *
     */
    private void publishPendingRequests() {
        List<DeliveryRequest> requests = deliveryRequestRepository.findByStatusOrderByCreatedAtAsc(DeliveryStatus.PENDING);
        if (requests.isEmpty()) {
            return;
        }

        log.info("Publishing Pending delivery requests. count= {}", requests.size());

        requests.forEach(deliveryQueuePublisher::publish);
    }

    /**
     * Retry 상태의 Delivery Request Publish
     * <p>
     * 조건<br/>
     * - status = RETRY<br/>
     * - nextAttemptAt <= 현재 시간
     *
     */
    private void publishRetryRequests() {
        List<DeliveryRequest> requests = deliveryRequestRepository
                .findByStatusAndNextAttemptAtLessThanEqualOrderByCreatedAtAsc(DeliveryStatus.RETRY, LocalDateTime.now());

        if (requests.isEmpty()) {
            return;
        }

        log.info("Publishing Retry delivery requests. count= {}", requests.size());

        requests.forEach(deliveryQueuePublisher::publish);
    }
}
