package com.rxspringboot.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
//@WebFluxTest
public class FluxAndMonoControllerTest {


    @Autowired
    private WebTestClient client;


    @Test
    public void fluxApproach1Test() {
        Flux<Integer> result = client.get().uri("/flux").accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk().returnResult(Integer.class).getResponseBody();

        StepVerifier.create(result).expectNext(1,2,3,4).verifyComplete();

    }


    @Test
    public void fluxApproach2Test() {
       client.get().uri("/flux").accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(4);

    }

    @Test
    public void fluxApproach3Test() {
        EntityExchangeResult<List<Integer>> result = client.get().uri("/flux").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .returnResult();

        Assertions.assertEquals(Arrays.asList(1,2,3,4), result.getResponseBody());

    }

    @Test
    public void fluxApproach4Test() {
        client.get().uri("/flux").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .consumeWith(response ->Assertions.assertEquals(Arrays.asList(1,2,3,4), response.getResponseBody()));
    }


    @Test
    public void fluxApproachStreamTest() {
        Flux<Integer> result = client.get().uri("/fluxstream").accept(MediaType.APPLICATION_STREAM_JSON).exchange()
                .expectStatus().isOk().returnResult(Integer.class).getResponseBody();

        StepVerifier.create(result).expectNext(1,2).thenCancel().verify();

    }

    @Test
    public void monoTest() {
        client.get().uri("/mono").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(response ->Assertions.assertEquals(1, response.getResponseBody()));
    }



}
