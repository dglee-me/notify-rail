package kr.co.dglee.notify.api.error;

import java.util.List;

public record ApiErrorResponse(
        String code,
        String message,
        List<FieldErrorResponse> error
) {
}

