package kr.co.dglee.notify.delivery;

public enum DeliveryStatus {
    QUEUED,         // 발송 요청 생성 및 처리 전
    DELIVERING,     // 발송 처리 중
    DELIVERED,      // 발송 성공 (완료)
    RETRY,          // 발송 실패 후 재시도 예정
    DEAD_LETTERED,  // 최종 발송 실패 (완료)
}
