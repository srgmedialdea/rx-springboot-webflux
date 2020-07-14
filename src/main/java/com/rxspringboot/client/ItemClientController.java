package com.rxspringboot.client;

import com.rxspringboot.document.Item;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemClientController {


    private WebClient client = WebClient.create(("http://localhost:8080"));

    @GetMapping("/rxclient/retrieve")
    public Flux<Item> retrieveAllItems() {
        return this.client.get().uri("restrx/items").retrieve().bodyToFlux(Item.class)
                .log("Items retrieved by webclient");
    }

    @GetMapping("/rxclient/exchange")
    public Flux<Item> exchangeAllItems() {
        return this.client.get().uri("restrx/items").exchange().flatMapMany(
                clientResponse -> clientResponse.bodyToFlux(Item.class)).log("Items exchanged by webclient");
    }

    @GetMapping("/rxclient/items/retrieve/{id}")
    public Mono<Item> retrieveAllItems(@PathVariable String id) {

        return this.client.get().uri("restrx/items/".concat(id)).retrieve().bodyToMono(Item.class)
                .log("Item retrieved by webclient");
    }

    @GetMapping("/rxclient/items/exchange/{id}")
    public Mono<Item> exchangeAllItems(@PathVariable String id) {
        return this.client.get().uri("restrx/items/".concat(id)).exchange().flatMap(
                clientResponse -> clientResponse.bodyToMono(Item.class)).log("Item exchanged by webclient");
    }

    @PostMapping("/rxclient/items")
    public Mono<Item> addItem(@RequestBody Item item) {

        return this.client.post().uri("restrx/items")
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(item), Item.class).retrieve().bodyToMono(Item.class)
                .log("Item added is: ");
    }

    @PutMapping("/rxclient/items/{id}")
    public Mono<Item> updateItem(@RequestBody Item item, @PathVariable String id) {

        return this.client.put().uri("restrx/items/{id}", id)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(item), Item.class).retrieve().bodyToMono(Item.class)
                .log("Item updated is: ");
    }

    @DeleteMapping("/rxclient/items/{id}")
    public Mono<Void> deletItem(@PathVariable String id) {

        return this.client.delete().uri("restrx/items/{id}", id)
                .retrieve().bodyToMono(Void.class);
    }

}
