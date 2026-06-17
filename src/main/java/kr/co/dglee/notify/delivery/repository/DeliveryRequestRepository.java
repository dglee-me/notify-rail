package kr.co.dglee.notify.delivery.repository;

import kr.co.dglee.notify.delivery.entity.DeliveryRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRequestRepository extends JpaRepository<DeliveryRequest, Long> {
}
