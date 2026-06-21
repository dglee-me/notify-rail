package kr.co.dglee.notify.worker.delivery.sender;

import kr.co.dglee.notify.domain.delivery.DeliveryChannel;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import org.springframework.stereotype.Component;

@Component
public class EmailSender implements Sender {
    @Override
    public boolean isSupport(DeliveryChannel channel) {
        return DeliveryChannel.EMAIL.equals(channel);
    }

    @Override
    public void send(DeliveryRequest request) {
        // TODO
    }
}
