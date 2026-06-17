package kr.co.dglee.notify.event.dto;

import java.util.UUID;
import kr.co.dglee.notify.event.entity.Event;

public record EventCreateResponse(
        UUID eventKey
) {

    public static EventCreateResponse from(Event event) {
        return new EventCreateResponse(event.getEventKey());
    }
}
