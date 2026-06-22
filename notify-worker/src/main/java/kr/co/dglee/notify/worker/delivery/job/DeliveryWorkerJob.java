package kr.co.dglee.notify.worker.delivery.job;

import java.time.LocalDateTime;
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
        // 큐 처리 (QUEUED)
        deliveryRequestRepository
                .findByStatusOrderByCreatedAtAsc(DeliveryStatus.QUEUED)
                .forEach(this::delivery);
    }

    private void delivery(DeliveryRequest request) {
        try {
            request.markDelivering();
            processor.process(request);
            request.markDelivered();
        } catch (Exception e) {
            if (request.canRetry()) {
                request.markRetry(LocalDateTime.now().plusMinutes(1)); // 1분 후 재시도하도록 설정
                return;
            }
            request.markDeadLettered();
        }
    }
}
