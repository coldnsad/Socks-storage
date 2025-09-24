package org.example.socks.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfiguration {

    private final Environment environment;

    @Bean
    public OpenAPI getOpenAPI() {
        Server server = new Server();
        server.setUrl(environment.getProperty("api.server.url"));
        server.setDescription("Test server");

        Contact contact = new Contact();
        contact.setName("Vladislav");
        contact.setUrl("https://github.com/coldnsad");

        Info info = new Info()
                .title("Api для работы с фабрикой носков")
                .version("1.0")
                .description("Api предоставляет эндпоинты для взаимодействия с фабрикой носков")
                .contact(contact);
        return new OpenAPI().info(info).servers(List.of(server));
    }
}
