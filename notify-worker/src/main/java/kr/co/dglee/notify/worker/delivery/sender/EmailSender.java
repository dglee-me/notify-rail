package kr.co.dglee.notify.worker.delivery.sender;

import kr.co.dglee.notify.domain.delivery.DeliveryChannel;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSender implements Sender {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;

    @Override
    public boolean isSupport(DeliveryChannel channel) {
        return DeliveryChannel.EMAIL.equals(channel);
    }

    @Override
    public void send(DeliveryRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(request.getTarget());
        message.setSubject("[Notify-Rail] Notification");
        message.setText("Notification // TODO - change Template");

        mailSender.send(message);
    }
}
