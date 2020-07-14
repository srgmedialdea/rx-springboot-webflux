package com.rxspringboot.controller;

import com.rxspringboot.document.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getAllItems() {
        webTestClient.get().uri("/rest/items").exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(6).consumeWith(response -> response.getResponseBody().forEach(
                        item -> Assertions.assertTrue(Objects.nonNull(item.getId()))));
    }

    @Test
    public void getOneItem() {
        webTestClient.get().uri("/rest/items/ABC").exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Item.class)
                .consumeWith(response -> Assertions.assertTrue(response.getResponseBody().getId().equals("ABC")));
    }

    @Test
    public void getOneItemNotFound() {
        webTestClient.get().uri("/rest/items/DEF").exchange().expectStatus().isNotFound();
    }

    @Test
    public void addItem() {
        webTestClient.post().uri("/rest/items").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Item(null, "IPhoneX", 499.99)), Item.class)
                .exchange().expectStatus().isCreated().expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("IPhoneX")
                .jsonPath(("$.price")).isEqualTo(499.99);
    }


    @Test
    public void deleteItem() {
        webTestClient.delete().uri("/rest/items/ABC")
                .exchange().expectStatus().isNoContent().expectBody(Void.class);
    }

    @Test
    public void deleteItemNotFound() {
        webTestClient.delete().uri("/rest/items/ABCx")
                .exchange().expectStatus().isNotFound().expectBody(Void.class);
    }

    @Test
    public void updateItem() {

        webTestClient.put().uri("/rest/items/ABC").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Item(null, "IPhoneX", 499.99)), Item.class)
                .exchange().expectStatus().isOk().expectBody(Item.class);
    }

    @Test
    public void updateItemNotFound() {
        webTestClient.put().uri("/rest/items/xyz").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Item(null, "IPhoneX", 499.99)), Item.class)
                .exchange().expectStatus().isNotFound().expectBody(Void.class);
    }


}
