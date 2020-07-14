package com.rxspringboot.handler;

import com.rxspringboot.document.Item;
import com.rxspringboot.initialiazer.ItemDataInitializer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemHandlerTest {


    @Autowired
    private ItemDataInitializer itemDataInitializer;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    public void setUp() throws Exception {
        //itemDataInitializer.run();
    }

    @Test
    @Order(1)
    public void getAllItems() {
        webTestClient.get().uri("/funct/items").exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(5).consumeWith(response -> response.getResponseBody().forEach(
                item -> Assertions.assertTrue(Objects.nonNull(item.getId()))));
    }

    @Test
    @Order(2)
    public void getOneItem() {
        webTestClient.get().uri("/funct/items/ABC").exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Item.class)
                .consumeWith(response ->
                {
                    Assertions.assertTrue(response.getResponseBody().getId().equals("ABC"));
                });
    }

    @Test
    @Order(3)
    public void getOneItemNotFound() {
               webTestClient.get().uri("/funct/items/pqr").exchange().expectStatus().isNotFound();
    }

    @Test
    @Order(4)
    public void addItem() {

        webTestClient.post().uri("/funct/items").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Item(null, "IPhoneX", 499.99)), Item.class)
                .exchange().expectStatus().isCreated().expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("IPhoneX")
                .jsonPath(("$.price")).isEqualTo(499.99);
    }

    @Test
    @Order(5)
    public void deleteItemNotFound() {
        webTestClient.delete().uri("/funct/items/pqr").exchange().expectStatus().isNotFound();
    }

    @Test
    @Order(6)
    public void deleteItem() {
        webTestClient.delete().uri("/funct/items/ABC").accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);
    }

    @Test
    @Order(7)
    public void updateItem() {

        webTestClient.put().uri("/funct/items/xyz").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Item(null, "Fenix 6 LTE", 699.99)), Item.class)
                .exchange().expectStatus().isOk().expectBody(Item.class)
                .consumeWith(response -> {
                    Assertions.assertTrue(response.getResponseBody().getId().equals("xyz"));
                    Assertions.assertTrue(response.getResponseBody().getDescription().equals("Fenix 6 LTE"));
                    Assertions.assertTrue(response.getResponseBody().getPrice().equals(699.99));
                });
    }

    @Test
    @Order(8)
    public void updateItemNotFound() {

        webTestClient.put().uri("/funct/items/pqr").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Item(null, "Fenix 6 LTE", 699.99)), Item.class)
                .exchange().expectStatus().isNotFound();
    }








}
