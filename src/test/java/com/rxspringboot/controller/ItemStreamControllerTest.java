package com.rxspringboot.controller;

import com.rxspringboot.document.Item;
import com.rxspringboot.document.ItemCapped;
import com.rxspringboot.repository.ItemCappedRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
@DirtiesContext
public class ItemStreamControllerTest {

    @Autowired
    private ReactiveMongoOperations rxMongoOps;
    @Autowired
    private ItemCappedRepository itemCappedRepo;
    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    public void setUp() {
        rxMongoOps.dropCollection(ItemCapped.class);
        rxMongoOps.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());
        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofSeconds(1))
                .map(i -> new ItemCapped(null, "RandomItem" + i, i.doubleValue()));
        itemCappedRepo.insert(itemCappedFlux)
                .doOnNext(itemCapped -> log.info("Inserted capped item is {}", itemCapped)).blockLast();
    }

    @Test
    public void getAllItems() {
        Flux<ItemCapped> result = webTestClient.get().uri("/stream/items").exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
                .returnResult(ItemCapped.class)
                .getResponseBody().take(5);
        StepVerifier.create(result).expectNextCount(5).thenCancel().verify();
    }

}
