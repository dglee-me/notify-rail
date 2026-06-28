package kr.co.dglee.notify.domain.delivery.validation;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import kr.co.dglee.notify.domain.delivery.DeliveryChannel;

public class DeliveryTargetValidator {

    public void validate(DeliveryChannel channel, String target) {
        switch (channel) {
            case EMAIL -> validateEmail(target);
            case SLACK, WEBHOOK -> validateHttp(target);
        }
    }

    private void validateEmail(String target) {
        if (target == null || target.isBlank()) {
            throw new DeliveryTargetValidationException("Delivery target email is empty");
        }

        if (!target.contains("@")) {
            throw new DeliveryTargetValidationException("Delivery target email is invalid");
        }
    }

    private void validateHttp(String target) {
        if (target == null || target.isBlank()) {
            throw new DeliveryTargetValidationException("Delivery target http is empty");
        }

        URI uri = parseUri(target);

        String scheme = uri.getScheme();
        String host = uri.getHost();

        if (scheme == null || host == null || host.isBlank()) {
            throw new DeliveryTargetValidationException("Delivery target must include scheme and host");
        }

        String normalizedScheme = scheme.toLowerCase();
        if (!normalizedScheme.equals("https")) {
            throw new DeliveryTargetValidationException("Delivery target must include https scheme");
        }

        InetAddress[] addresses;
        try {
            addresses = InetAddress.getAllByName(host);
        } catch (UnknownHostException e) {
            throw new DeliveryTargetValidationException("Delivery target host could not be resolved");
        }

        for (InetAddress address : addresses) {
            if (isBlockedAddress(address)) {
                throw new DeliveryTargetValidationException("Delivery target address is blocked");
            }
        }
    }

    private URI parseUri(String target) {
        try {
            return URI.create(target);
        } catch (IllegalArgumentException e) {
            throw new DeliveryTargetValidationException("Invalid delivery target URL");
        }
    }

    private boolean isBlockedAddress(InetAddress address) {
        return address.isAnyLocalAddress()      // 0.0.0.0, ::
                || address.isLoopbackAddress() // 127.0.0.1, ::1
                || address.isLinkLocalAddress() // 169.254.x.x, fe80::/10
                || address.isSiteLocalAddress() // 10/8, 172.16/12, 192.168/16
                || address.isMulticastAddress(); // multicast
    }
}
