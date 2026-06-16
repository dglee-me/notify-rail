package kr.co.dglee.notify.event.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import kr.co.dglee.notify.common.domain.BaseTimeEntity;
import kr.co.dglee.notify.event.EventStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "events",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_events_source_idempotency_key",
                        columnNames = {"source", "idempotency_key"}
                ),
                @UniqueConstraint(
                        name = "uk_events_event_key",
                        columnNames = "event_key"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_key", nullable = false, updatable = false)
    private UUID eventKey;

    @Column(name = "idempotency_key", nullable = false, length = 200)
    private String idempotencyKey;

    @Column(nullable = false, length = 100)
    private String source;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EventStatus status;

    @Lob
    @Column(nullable = false)
    private String payload;

    public static Event receive(String source, String eventType, String idempotencyKey, String payload) {
        Event event = new Event();
        event.eventKey = UUID.randomUUID();
        event.idempotencyKey = idempotencyKey;
        event.source = source;
        event.eventType = eventType;
        event.status = EventStatus.RECEIVED;
        event.payload = payload;
        return event;
    }
}
