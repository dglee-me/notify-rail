package kr.co.dglee.notify.api.event.service;

import java.util.Optional;
import kr.co.dglee.notify.api.delivery.repository.DeliveryRequestRepository;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import kr.co.dglee.notify.domain.event.entity.Event;
import kr.co.dglee.notify.api.event.dto.EventCreateRequest;
import kr.co.dglee.notify.api.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final DeliveryRequestRepository deliveryRequestRepository;

    private final ObjectMapper objectMapper;

    @Transactional
    public Event createEvent(EventCreateRequest createRequest) {
        Optional<Event> existEvent = eventRepository.findBySourceAndIdempotencyKey(createRequest.source(),
                createRequest.idempotencyKey());
        if (existEvent.isPresent()) {
            return existEvent.get();
        }

        Event event = Event.receive(
                createRequest.source(),
                createRequest.eventType(),
                createRequest.idempotencyKey(),
                objectMapper.writeValueAsString(createRequest.payload()));
        eventRepository.save(event);

        deliveryRequestRepository.saveAll(
                createRequest.recipients()
                        .stream()
                        .map(recipient -> DeliveryRequest.queue(event, recipient.channel(), recipient.target()))
                        .toList()
        );
        return event;
    }
}
