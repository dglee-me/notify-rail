package kr.co.dglee.notify.common.error;

public record FieldErrorResponse(
        String field,
        String message
) {
}
