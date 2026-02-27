// package com.hcl.linklite.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.info.Contact;
// import io.swagger.v3.oas.models.info.Info;
// import io.swagger.v3.oas.models.info.License;
// import io.swagger.v3.oas.models.servers.Server;

// @Configuration
// public class OpenApiConfig {

//     @Bean
//     public OpenAPI customOpenAPI() {
//         return new OpenAPI()
//                 .info(new Info()
//                         .title("LinkLite API")
//                         .description("URL Shortener with Analytics System - REST API Documentation")
//                         .version("1.0.0")
//                         .contact(new Contact()
//                                 .name("LinkLite Support")
//                                 .email("support@linklite.com")
//                                 .url("https://linklite.com"))
//                         .license(new License()
//                                 .name("Apache 2.0")
//                                 .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
//                 .addServersItem(new Server()
//                         .url("http://localhost:8081")
//                         .description("Development Server"))
//                 .addServersItem(new Server()
//                         .url("https://api.example.com")
//                         .description("Production Server"));
//     }
// }
