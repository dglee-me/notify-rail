package kr.co.dglee.notify.worker.delivery.sender;

import java.util.LinkedHashMap;
import java.util.Map;
import kr.co.dglee.notify.domain.delivery.DeliveryChannel;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import kr.co.dglee.notify.domain.event.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class WebhookSender implements Sender {

    private final RestClient webhookRestClient;
    private final ObjectMapper objectMapper;

    @Override
    public boolean isSupport(DeliveryChannel channel) {
        return DeliveryChannel.WEBHOOK.equals(channel);
    }

    @Override
    public void send(DeliveryRequest request) {
        Event event = request.getEvent();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("eventKey", event.getEventKey());
        body.put("source", event.getSource());
        body.put("eventType", event.getEventType());
        body.put("payload", objectMapper.readTree(event.getPayload()));

        webhookRestClient
                .post()
                .uri(request.getTarget())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}
