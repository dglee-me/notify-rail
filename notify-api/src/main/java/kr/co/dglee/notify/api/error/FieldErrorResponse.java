package kr.co.dglee.notify.api.error;

public record FieldErrorResponse(
        String field,
        String message
) {
}
