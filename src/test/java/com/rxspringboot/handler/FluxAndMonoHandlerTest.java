package com.rxspringboot.handler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class FluxAndMonoHandlerTest {


    @Autowired
    private WebTestClient client;


    @Test
    public void fluxHandlerTest() {
        Flux<Integer> result = client.get().uri("/functional/flux").accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk().returnResult(Integer.class).getResponseBody();

        StepVerifier.create(result).expectNext(1,2,3,4).verifyComplete();

    }

    @Test
    public void monoHandlerTest() {
        client.get().uri("/functional/mono").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(response ->Assertions.assertEquals(1, response.getResponseBody()));
    }








}
