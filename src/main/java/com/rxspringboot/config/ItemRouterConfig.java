package com.rxspringboot.config;

import com.rxspringboot.handler.ItemHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ItemRouterConfig {

   @Bean
    public RouterFunction<ServerResponse> itemRoute(ItemHandler itemHandler) {
       return RouterFunctions
               .route(GET("funct/items").and(accept(MediaType.APPLICATION_JSON)), itemHandler::getAllItems)
               .andRoute(GET("funct/items/{id}").and(accept(MediaType.APPLICATION_JSON)), itemHandler::getItemById)
               .andRoute(POST("funct/items").and(accept(MediaType.APPLICATION_JSON)), itemHandler::addItem)
               .andRoute(DELETE("funct/items/{id}").and(accept(MediaType.APPLICATION_JSON)), itemHandler::deleteItem)
               .andRoute(PUT("funct/items/{id}").and(accept(MediaType.APPLICATION_JSON)), itemHandler::updateItem)
               .andRoute(GET("funct/stream/items").and(accept(MediaType.APPLICATION_JSON)), itemHandler::itemStream);
   }
}
