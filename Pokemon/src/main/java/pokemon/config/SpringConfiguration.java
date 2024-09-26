package pokemon.config;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.Collections;


@Configuration
public class SpringConfiguration implements WebMvcConfigurer{

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = restTemplateBuilder
                .setReadTimeout(Duration.ofSeconds(5))
                .setConnectTimeout(Duration.ofSeconds(5))
                .build();
        restTemplate.setRequestFactory(factory);
        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateRequestResponseLogger()));
        return restTemplate;
    }
}

