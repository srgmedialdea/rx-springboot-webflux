package com.rxspringboot.config;

import com.rxspringboot.handler.FluxAndMonoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class FluxAndMonoRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> route(FluxAndMonoHandler handler) {
        return RouterFunctions
                .route(GET("/functional/flux").and(accept(MediaType.APPLICATION_JSON)), handler::fluxExample)
                .andRoute(GET("/functional/mono").and(accept(MediaType.APPLICATION_JSON)), handler::monoExample);

    }


}
