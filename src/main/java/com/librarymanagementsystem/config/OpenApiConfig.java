package com.librarymanagementsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Fares",
                        email = "fares.gmahmoud@gmail.com"
                ),
                description = "OpenApi documentation for Springboot Fintech MVP Application",
                title = "API Design Documentation",
                version = "1.0.0"
        ),
        servers = @Server(
                description = "Local ENV",
                url = "http://localhost:8088"
        )

)
public class OpenApiConfig
{


}