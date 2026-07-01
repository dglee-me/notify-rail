package kr.co.dglee.notify.worker.delivery.consumer;

import kr.co.dglee.notify.worker.delivery.handler.DeliveryRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "notify.queue.type", havingValue = "rabbitmq")
@RequiredArgsConstructor
public class RabbitMQDeliveryConsumer {

    private final DeliveryRequestHandler handler;

    @RabbitListener(queues = "${notify.rabbitmq.delivery-queue}")
    public void consume(Long deliveryRequestId) {
        handler.handle(deliveryRequestId);
    }
}
