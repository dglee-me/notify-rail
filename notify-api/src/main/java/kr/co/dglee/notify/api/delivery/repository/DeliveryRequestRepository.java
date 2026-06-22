package kr.co.dglee.notify.api.delivery.repository;

import java.time.LocalDateTime;
import java.util.List;
import kr.co.dglee.notify.domain.delivery.DeliveryStatus;
import kr.co.dglee.notify.domain.delivery.entity.DeliveryRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRequestRepository extends JpaRepository<DeliveryRequest, Long> {

    List<DeliveryRequest> findByStatusOrderByCreatedAtAsc(DeliveryStatus status);

    List<DeliveryRequest> findByStatusAndNextAttemptAtLessThanEqualOrderByCreatedAtAsc(DeliveryStatus status, LocalDateTime nextAttemptAt);
}
