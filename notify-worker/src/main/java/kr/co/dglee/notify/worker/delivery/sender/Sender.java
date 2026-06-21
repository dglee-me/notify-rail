package kr.co.dglee.notify.worker.delivery.sender;

import kr.co.dglee.notify.domain.delivery.DeliveryChannel;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;

public interface Sender {

    boolean isSupport(DeliveryChannel channel);

    void send(DeliveryRequest request);
}
