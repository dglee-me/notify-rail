package kr.co.dglee.notify.api.error;

import java.util.List;
import kr.co.dglee.notify.domain.delivery.validation.DeliveryTargetValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleValidationExceptions(MethodArgumentNotValidException exception) {
        List<FieldErrorResponse> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();

        return new ApiErrorResponse("VALIDATION_FAILED", "Request validation failed", errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DeliveryTargetValidationException.class)
    public ApiErrorResponse handleDeliveryTargetValidationException(DeliveryTargetValidationException exception) {
        return new ApiErrorResponse(
                "INVALID_DELIVERY_TARGET",
                exception.getMessage(),
                List.of()
        );
    }
}
