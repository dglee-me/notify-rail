package kr.co.dglee.notify.domain.delivery;

public enum DeliveryStatus {
    PENDING,        // 발송 요청 생성, 아직 큐 발행 전
    QUEUED,         // 큐 발행 완료, 워커 처리 대기
    DELIVERING,     // 워커가 발송 처리 중
    DELIVERED,      // 발송 성공 (완료)
    RETRY,          // 발송 실패 후 재시도 예정
    DEAD_LETTERED,  // 최종 발송 실패 (완료)
}
