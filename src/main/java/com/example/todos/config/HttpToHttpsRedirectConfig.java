package com.example.todos.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpToHttpsRedirectConfig {

    @Bean
    public ServletWebServerFactory servletContainer(
            @Value("${server.port:8443}") int httpsPort,
            @Value("${http.port:8080}") int httpPort) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Connector http = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        http.setScheme("http");
        http.setPort(httpPort);
        http.setSecure(false);
        http.setRedirectPort(httpsPort);
        tomcat.addAdditionalTomcatConnectors(http);
        return tomcat;
    }
}

