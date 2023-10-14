package com.khandras.bot.fw.props;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppProps {
    @Bean
    public ObjectMapper objectMapper() {
        var json = new ObjectMapper();
        json.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        json.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        json.registerModule(new JavaTimeModule());
        json.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return json;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
