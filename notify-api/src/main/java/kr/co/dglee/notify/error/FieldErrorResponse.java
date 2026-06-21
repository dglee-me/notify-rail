package kr.co.dglee.notify.error;

public record FieldErrorResponse(
        String field,
        String message
) {
}
