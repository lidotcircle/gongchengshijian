package six.daoyun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;


@Configuration
public class SpringDocConfig {
    /*
    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (Operation operation, HandlerMethod handlerMethod) -> {

            Parameter authHeader = new Parameter()
                .in(ParameterIn.HEADER.toString())
                .schema(new StringSchema())
                .name("Authorization").description("JSON Web Token")
                .required(true);
                    
            operation.addParametersItem(authHeader);

            return operation;
        };
    }    
    */

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Daoyun").version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("JSON Web Token", new SecurityScheme()
                            .type(SecurityScheme.Type.APIKEY)
                            .in(SecurityScheme.In.HEADER)
                            .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("JSON Web Token"));
    }
}

