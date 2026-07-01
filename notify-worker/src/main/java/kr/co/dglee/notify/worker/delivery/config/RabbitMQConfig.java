package kr.co.dglee.notify.worker.delivery.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RabbitMQConfig.RabbitMQProperties.class})
public class RabbitMQConfig {

    @Bean
    public DirectExchange deliveryExchange(RabbitMQProperties properties) {
        return new DirectExchange(properties.deliveryExchange(), true, false);
    }

    @Bean
    public Queue deliveryQueue(RabbitMQProperties properties) {
        return QueueBuilder.durable(properties.deliveryQueue()).build();
    }

    @Bean
    public Binding deliveryBinding(Queue deliveryQueue, DirectExchange deliveryExchange,
                                   RabbitMQProperties properties) {
        return BindingBuilder
                .bind(deliveryQueue)
                .to(deliveryExchange)
                .with(properties.deliveryRoutingKey());
    }

    @ConfigurationProperties(prefix = "notify.rabbitmq")
    public record RabbitMQProperties(
            String deliveryExchange,
            String deliveryQueue,
            String deliveryRoutingKey
    ) {
    }
}
