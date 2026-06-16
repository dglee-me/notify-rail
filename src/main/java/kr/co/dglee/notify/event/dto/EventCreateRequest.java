package kr.co.dglee.notify.event.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tools.jackson.databind.JsonNode;

public record EventCreateRequest(
        @NotBlank @Size(max = 100) String source,
        @NotBlank @Size(max = 100) String eventType,
        @NotBlank @Size(max = 200) String idempotencyKey,
        @NotNull JsonNode payload
) {

    @AssertTrue(message = "payload must be a JSON object")
    public boolean isPayloadObject() {
        return payload == null || payload.isObject();
    }
}
