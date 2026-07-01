package kr.co.dglee.notify.worker.delivery.job;

import java.util.concurrent.Executor;
import kr.co.dglee.notify.worker.delivery.handler.DeliveryRequestHandler;
import kr.co.dglee.notify.worker.delivery.service.DeliveryWorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "notify.queue.type", havingValue = "db", matchIfMissing = true)
@RequiredArgsConstructor
public class DbDeliveryWorkerJob {

    private final DeliveryWorkerService deliveryWorkerService;

    private final DeliveryRequestHandler handler;

    private final Executor deliveryExecutor;

    @Scheduled(fixedDelay = 1000)
    public void consumeDeliveryRequests() {
        // 큐 처리 (QUEUED)
        deliveryWorkerService
                .findQueuedRequests()
                .forEach(request -> deliveryExecutor.execute(() -> handler.handle(request.getId())));
    }
}
