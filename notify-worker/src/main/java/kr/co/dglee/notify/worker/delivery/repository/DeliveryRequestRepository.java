package kr.co.dglee.notify.worker.delivery.repository;

import java.util.List;
import java.util.Optional;
import kr.co.dglee.notify.domain.delivery.DeliveryStatus;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRequestRepository extends JpaRepository<DeliveryRequest, Long> {

    List<DeliveryRequest> findByStatusOrderByCreatedAtAsc(DeliveryStatus status);

    @EntityGraph(attributePaths = "event")
    Optional<DeliveryRequest> findWithEventById(Long id);
}
