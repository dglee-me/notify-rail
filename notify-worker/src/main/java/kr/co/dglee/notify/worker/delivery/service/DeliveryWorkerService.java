package kr.co.dglee.notify.worker.delivery.service;

import java.time.LocalDateTime;
import java.util.List;
import kr.co.dglee.notify.domain.delivery.DeliveryStatus;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import kr.co.dglee.notify.worker.delivery.repository.DeliveryRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class DeliveryWorkerService {

    private final DeliveryRequestRepository deliveryRequestRepository;

    public List<DeliveryRequest> findQueuedRequests() {

        return deliveryRequestRepository
                .findByStatusOrderByCreatedAtAsc(DeliveryStatus.QUEUED);
    }

    @Transactional
    public DeliveryRequest markDelivering(Long id) {
        DeliveryRequest request = deliveryRequestRepository.findWithEventById(id).orElseThrow();
        request.markDelivering();

        return request;
    }

    @Transactional
    public void markDelivered(Long id) {
        deliveryRequestRepository
                .findById(id)
                .orElseThrow()
                .markDelivered();
    }

    @Transactional
    public void markFailed(Long id) {
        DeliveryRequest request = deliveryRequestRepository.findById(id).orElseThrow();

        // 재시도 가능하다면 재시도 처리
        if (request.canRetry()) {
            request.markRetry(LocalDateTime.now().plusMinutes(1)); // 1분 후 재시도하도록 설정
            return;
        }

        // 재시도가 불가능하다면 DEAD_LETTER 처리
        request.markDeadLettered();

        log.warn("Delivery request {} has failed and moved to dead letter queue.", id);
    }

    @Transactional
    public void markDeadLettered(Long id) {
        DeliveryRequest request = deliveryRequestRepository.findById(id).orElseThrow();
        request.markDeadLettered();
    }
}
