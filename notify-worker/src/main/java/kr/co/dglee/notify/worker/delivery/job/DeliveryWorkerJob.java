package kr.co.dglee.notify.worker.delivery.job;

import kr.co.dglee.notify.domain.delivery.DeliveryStatus;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import kr.co.dglee.notify.worker.delivery.processor.DeliveryProcessor;
import kr.co.dglee.notify.worker.delivery.repository.DeliveryRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeliveryWorkerJob {

    private final DeliveryRequestRepository deliveryRequestRepository;
    private final DeliveryProcessor processor;

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public void consumeDeliveryRequests() {
        deliveryRequestRepository
                .findByStatusOrderByCreatedAtAsc(DeliveryStatus.QUEUED)
                .forEach(this::delivery);
    }

    private void delivery(DeliveryRequest request) {
        request.markDelivering();
        processor.process(request);
        request.markDelivered();
    }
}
