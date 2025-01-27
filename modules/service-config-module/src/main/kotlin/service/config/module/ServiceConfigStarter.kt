package service.config.module

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@ComponentScan
@AutoConfigureBefore(JacksonAutoConfiguration::class)
class ServiceConfigStarter {

    @Bean
    fun jackson2ObjectMapperBuilderCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
        return Jackson2ObjectMapperBuilderCustomizer { builder: Jackson2ObjectMapperBuilder ->
            builder.serializationInclusion(JsonInclude.Include.NON_NULL)
            builder.featuresToDisable(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                SerializationFeature.FAIL_ON_EMPTY_BEANS
            )
        }
    }
}