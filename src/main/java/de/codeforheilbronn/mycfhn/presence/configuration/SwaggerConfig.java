package de.codeforheilbronn.mycfhn.presence.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

     @Bean
    public Docket swaggerConf() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("de.codeforheilbronn.mycfhn.presence"))
                .build()
                .directModelSubstitute(LocalDateTime.class, String.class);
    }
}
