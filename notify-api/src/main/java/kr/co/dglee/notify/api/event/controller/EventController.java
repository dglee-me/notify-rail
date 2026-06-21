package kr.co.dglee.notify.api.event.controller;

import jakarta.validation.Valid;
import kr.co.dglee.notify.api.event.dto.EventCreateRequest;
import kr.co.dglee.notify.api.event.dto.EventCreateResponse;
import kr.co.dglee.notify.domain.event.entity.Event;
import kr.co.dglee.notify.api.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/events")
@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventCreateResponse> createEvent(@Valid @RequestBody EventCreateRequest createRequest) {

        Event event = eventService.createEvent(createRequest);
        return ResponseEntity
                .accepted()
                .body(EventCreateResponse.from(event));
    }
}
