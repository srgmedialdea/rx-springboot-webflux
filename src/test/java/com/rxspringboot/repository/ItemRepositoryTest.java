package com.rxspringboot.repository;


import com.rxspringboot.document.Item;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepo;

    @BeforeAll
    public void  setup() {

        List<Item> itemList = Arrays.asList(
                new Item(null, "Samsung TV", 400.00),
                new Item(null, "LG TV", 420.00),
                new Item(null, "Apple Watch", 299.99),
                new Item(null, "Fenix5 Watch", 99.99),
                new Item("ABC", "Beats Headphones", 149.99));


        itemRepo.deleteAll().thenMany(Flux.fromIterable(itemList))
                .flatMap(itemRepo::save).doOnNext(item -> System.out.println("Inserted item is " + item))
                .blockLast();//Important, only for testing purpose, because we need the stream remains blocked
                             //to be available for test methods
    }


    @Test
    @Order(1)
    public void getAllItemsTest() {
        StepVerifier.create(itemRepo.findAll()).expectNextCount(5);
    }

    @Test
    @Order(2)
    public void findItemByIdTest() {
        StepVerifier.create(itemRepo.findById("ABC"))
                .expectNextMatches(item -> item.getDescription().equals("Beats Headphones"))
                .verifyComplete();
    }

    @Test
    @Order(3)
    public void findItemByDescTest() {
        StepVerifier.create(itemRepo.findByDescription("Fenix5 Watch").log("findByDescription: "))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Order(4)
    public void saveItemTest() {

        Item item = new Item("DEF", "Orbea Alma", 1499.99);

        Mono<Item> savedItem = itemRepo.save(item);

        StepVerifier.create(savedItem.log("savedItem: "))
                .expectNextMatches(i -> Objects.nonNull(i.getId()) && Objects.equals(i.getDescription(), item.getDescription()))
                .verifyComplete();
    }

    @Test
    @Order(5)
    public void updateItemTest() {


        String updatedItemDesc = "Beats Headphones Plus";
        Double updatedItemPrice = 185.99;


        Mono<Item> updatedItem = itemRepo.findById("ABC").map(foundedItem -> {
            foundedItem.setDescription(updatedItemDesc);
            foundedItem.setPrice(updatedItemPrice);
            return foundedItem;
        }).flatMap(itemToUpdate -> itemRepo.save(itemToUpdate));


        StepVerifier.create(updatedItem.log("updatedItem: "))
                .expectNextMatches(i -> Objects.nonNull(i.getId()) && Objects.equals(i.getDescription(), updatedItemDesc)
                        &&  Objects.equals(i.getPrice(), updatedItemPrice)).verifyComplete();
    }


    @Test
    @Order(6)
    public void deleteItemByIdTest() {

        Mono<Void> deletedItem = itemRepo.findById("DEF").flatMap(itemRepo::delete);

        StepVerifier.create(deletedItem.log("deletedItem: ")).verifyComplete();

        StepVerifier.create(itemRepo.findAll().log("findAll: ")).expectNextCount(5).verifyComplete();
    }

    @Test
    @Order(7)
    public void deleteItemByDescTest() {

        Mono<Void> deletedItem = itemRepo.findByDescription("LG TV").flatMap(itemRepo::delete);

        StepVerifier.create(deletedItem.log("deletedItem: ")).verifyComplete();

        StepVerifier.create(itemRepo.findAll().log("findAll: ")).expectNextCount(4).verifyComplete();
    }




}
