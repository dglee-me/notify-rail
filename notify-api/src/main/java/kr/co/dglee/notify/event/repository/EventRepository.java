package kr.co.dglee.notify.event.repository;

import java.util.Optional;
import kr.co.dglee.notify.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findBySourceAndIdempotencyKey(String source, String idempotencyKey);
}
