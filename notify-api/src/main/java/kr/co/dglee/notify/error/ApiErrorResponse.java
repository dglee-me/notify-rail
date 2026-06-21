package kr.co.dglee.notify.error;

import java.util.List;

public record ApiErrorResponse(
        String code,
        String message,
        List<FieldErrorResponse> error
) {
}

