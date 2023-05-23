package br.com.uniamerica.estacionamento.config;

import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonObjectMapperConfig {

    /**
     * Customizes the Jackson ObjectMapper to handle coercion of empty strings to null for Enum types.
     *
     * @return Jackson2ObjectMapperBuilderCustomizer instance.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizeObjectMapper() {
        return builder -> builder.postConfigurer(objectMapper -> {
            objectMapper.coercionConfigFor(LogicalType.Enum)
                    .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
        });
    }
}
