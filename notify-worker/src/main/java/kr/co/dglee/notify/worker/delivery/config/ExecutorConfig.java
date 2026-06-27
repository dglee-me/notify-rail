package kr.co.dglee.notify.worker.delivery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class ExecutorConfig {

    @Bean
    public AsyncTaskExecutor deliveryExecutor(
            @Value("${notify.worker.delivery.max-concurrency:10}") int maxConcurrency
    ) {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("delivery-");
        executor.setVirtualThreads(true);
        executor.setConcurrencyLimit(maxConcurrency);
        return executor;
    }
}
