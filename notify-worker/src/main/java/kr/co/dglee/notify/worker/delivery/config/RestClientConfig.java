package kr.co.dglee.notify.worker.delivery.config;

import java.util.Optional;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient webhookRestClient(
            RestClient.Builder builder,
            ObjectProvider<BuildProperties> buildProperties
    ) {
        String version = Optional.ofNullable(buildProperties.getIfAvailable())
                .map(BuildProperties::getVersion)
                .orElse("undefined");

        return builder
                .defaultHeader(HttpHeaders.USER_AGENT, "Notify-Rail/" + version)
                .build();
    }
}
