package kr.co.dglee.notify.event.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kr.co.dglee.notify.domain.delivery.DeliveryChannel;
import tools.jackson.databind.JsonNode;

public record EventCreateRequest(
        @NotBlank @Size(max = 100) String source,
        @NotBlank @Size(max = 100) String eventType,
        @NotBlank @Size(max = 200) String idempotencyKey,
        @NotEmpty @Size(min = 1, max = 100) List<@Valid @NotNull RecipientRequest> recipients,
        @NotNull JsonNode payload
) {
    @AssertTrue(message = "payload must be a JSON object")
    public boolean isPayloadObject() {
        return payload == null || payload.isObject();
    }

    @AssertTrue(message = "recipients must not contain duplicate channel and target")
    public boolean isRecipientsUnique() {
        if (recipients == null) {
            return true;
        }

        Set<String> keys = new HashSet<>();
        for (RecipientRequest recipient : recipients) {
            if (recipient == null || recipient.channel() == null || recipient.target() == null) {
                continue;
            }

            String key = recipient.channel() + ":" + recipient.target().trim();
            if (!keys.add(key)) {
                return false;
            }
        }

        return true;
    }

    public record RecipientRequest(
            @NotNull DeliveryChannel channel,
            @NotBlank @Size(max = 500) String target
    ) {
    }
}
