package za.co.pacifish.notification_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
            .requestInterceptor((request, body, execution) -> {
                log.info("Request URI: {}", request.getURI());
                log.info("Headers: {}", request.getHeaders());
                log.info("Body: {}", body);
                return execution.execute(request, body);
            })
            .build();
    }
}
