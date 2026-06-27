package kr.co.dglee.notify.worker.delivery.sender;

import java.util.LinkedHashMap;
import java.util.Map;
import kr.co.dglee.notify.domain.delivery.DeliveryChannel;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import kr.co.dglee.notify.domain.event.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class SlackSender implements Sender {

    private final RestClient webhookRestClient;
    private final ObjectMapper objectMapper;

    @Override
    public boolean isSupport(DeliveryChannel channel) {
        return DeliveryChannel.SLACK.equals(channel);
    }

    @Override
    public void send(DeliveryRequest request) {
        webhookRestClient
                .post()
                .uri(request.getTarget())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(createSlackCompatibleBody(request))
                .retrieve()
                .toBodilessEntity();
    }

    private MultiValueMap<String, String> createSlackCompatibleBody(DeliveryRequest request) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("username", "NotifyRail");
        payload.put("text", createSlackText(request));
        payload.put("icon_emoji", ":bell:");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("payload", objectMapper.writeValueAsString(payload));

        return body;
    }

    private String createSlackText(DeliveryRequest request) {
        Event event = request.getEvent();

        return """
                %s event received
                source: %s
                eventKey: %s
                payload: %s
                """.formatted(
                event.getEventType(),
                event.getSource(),
                event.getEventKey(),
                event.getPayload()
        );
    }
}
