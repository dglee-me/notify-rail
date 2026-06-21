package kr.co.dglee.notify.worker.delivery.processor;

import java.util.List;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import kr.co.dglee.notify.worker.delivery.sender.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryProcessor {

    private final List<Sender> senders;

    public void process(DeliveryRequest request) {
        Sender sender = senders.stream()
                .filter(candidate -> candidate.isSupport(request.getChannel()))
                .findFirst()
                .orElseThrow();

        sender.send(request);
    }
}
