package kr.co.dglee.notify.worker.delivery.job;

import java.net.URI;
import kr.co.dglee.notify.domain.delivery.DeliveryChannel;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import kr.co.dglee.notify.worker.delivery.processor.DeliveryProcessor;
import kr.co.dglee.notify.worker.delivery.service.DeliveryWorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryWorkerJob {

    private final DeliveryWorkerService deliveryWorkerService;

    private final DeliveryProcessor processor;

    @Scheduled(fixedDelay = 1000)
    public void consumeDeliveryRequests() {
        // 큐 처리 (QUEUED)
        deliveryWorkerService
                .findQueuedRequests()
                .forEach(this::delivery);
    }

    private void delivery(DeliveryRequest request) {
        DeliveryRequest targetRequest = deliveryWorkerService.markDelivering(request.getId());

        try {
            processor.process(targetRequest);
            deliveryWorkerService.markDelivered(targetRequest.getId());

            log.info("Delivery request processed successfully. deliveryRequestId={}, channel={}, target={}",
                    targetRequest.getId(),
                    targetRequest.getChannel(),
                    maskTarget(targetRequest.getChannel(), targetRequest.getTarget()));
        } catch (Exception e) {
            deliveryWorkerService.markFailed(request.getId());
            log.error("Failed to process delivery request with ID: {}", request.getId(), e);
        }
    }

    private String maskTarget(DeliveryChannel channel, String target) {
        if (channel == null) {
            return "***";
        }

        return switch (channel) {
            case EMAIL -> maskEmailTarget(target);
            case WEBHOOK -> maskWebhookTarget(target);
        };
    }

    private String maskEmailTarget(String target) {
        if (target == null || !target.contains("@")) {
            return "***";
        }

        String[] parts = target.split("@", 2);
        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.isBlank()) {
            return "***@" + domain;
        }

        if (localPart.length() <= 2) {
            return localPart.charAt(0) + "***@" + domain;
        }

        return localPart.substring(0, 2) + "***@" + domain;
    }

    private String maskWebhookTarget(String target) {
        if (target == null || target.isBlank()) {
            return "***";
        }

        try {
            URI uri = URI.create(target);
            String scheme = uri.getScheme();
            String host = uri.getHost();

            if (scheme == null || scheme.isBlank() || host == null || host.isBlank()) {
                return "***";
            }

            return scheme + "://" + host + "/...";
        } catch (IllegalArgumentException e) {
            return "***";
        }
    }
}
