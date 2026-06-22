package kr.co.dglee.notify.domain.delivery.entity;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import kr.co.dglee.notify.domain.BaseTimeEntity;
import kr.co.dglee.notify.domain.delivery.DeliveryChannel;
import kr.co.dglee.notify.domain.delivery.DeliveryStatus;
import kr.co.dglee.notify.domain.event.entity.Event;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "delivery_requests", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_delivery_requests_event_channel_target",
                columnNames = {"event_id", "channel", "target"}
        )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DeliveryStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DeliveryChannel channel;

    @Column(nullable = false, length = 500)
    private String target;

    @Column(name = "queue_published_at")
    private LocalDateTime queuePublishedAt;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount = 0;

    @Column(name = "next_attempt_at")
    private LocalDateTime nextAttemptAt;

    public static DeliveryRequest queue(Event event, DeliveryChannel channel, String target) {
        DeliveryRequest request = new DeliveryRequest();
        request.event = event;
        request.status = DeliveryStatus.PENDING;
        request.channel = channel;
        request.target = target;

        return request;
    }

    public void markQueuePublished(LocalDateTime publishedAt) {
        this.status = DeliveryStatus.QUEUED;
        this.queuePublishedAt = publishedAt;
        this.nextAttemptAt = null;
    }

    public void markDelivering() {
        this.status = DeliveryStatus.DELIVERING;
        this.attemptCount++;
    }

    public boolean canRetry() {
        return attemptCount < 3;
    }

    public void markRetry(LocalDateTime nextAttemptAt) {
        this.status = DeliveryStatus.RETRY;
        this.nextAttemptAt = nextAttemptAt;
    }

    public void markDeadLettered() {
        this.status = DeliveryStatus.DEAD_LETTERED;
        this.nextAttemptAt = null;
    }

    public void markDelivered() {
        this.status = DeliveryStatus.DELIVERED;
    }
}
