package com.rxspringboot.handler;

import com.rxspringboot.document.Item;
import com.rxspringboot.document.ItemCapped;
import com.rxspringboot.repository.ItemCappedRepository;
import com.rxspringboot.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.Objects;

@Component
public class ItemHandler {

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private ItemCappedRepository itemCappedRepo;

    public Mono<ServerResponse> getAllItems(ServerRequest requst) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(itemRepo.findAll(), Item.class);
    }


    public Mono<ServerResponse> getItemById(ServerRequest request) {
       /*
       //Approach 1
       return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(itemRepo.findById(request.pathVariable("id")), Item.class)
                .switchIfEmpty(ServerResponse.notFound().build());
        */

        String id = request.pathVariable("id");

     /*
     //Approach 2
     return itemRepo.findById(id).flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)).switchIfEmpty(ServerResponse.notFound().build());
     */

        //Approach 3
        return itemRepo.findById(id).flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .bodyValue(item)).switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> addItem(ServerRequest request) {

        Mono<Item> itemToSaved = request.bodyToMono(Item.class);

        return itemToSaved.flatMap(itemRepo::save).flatMap(savedItem ->
                ServerResponse.created(URI.create("funct/items/".concat(savedItem.getId())))
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(savedItem));


    }

    public Mono<ServerResponse> deleteItem(ServerRequest request) {

        String id = request.pathVariable("id");

        return itemRepo.findById(id).flatMap(item ->
                itemRepo.delete(item).then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> updateItem(ServerRequest request) {

        String id = request.pathVariable("id");
        Mono<Item> monoItem = request.bodyToMono(Item.class);

       return monoItem.flatMap(item -> itemRepo.findById(id).flatMap(itemDB -> {
           itemDB.setPrice(item.getPrice());
           itemDB.setDescription(item.getDescription());
           return Mono.just(itemDB);
        }).flatMap(itemToUpdate -> itemRepo.save(itemToUpdate)))
               .flatMap(updatedItem -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(updatedItem))
               .switchIfEmpty(ServerResponse.notFound().build());
    }


    public Mono<ServerResponse> itemStream(ServerRequest request) {

        return ServerResponse.ok().contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(itemCappedRepo.findItemsBy(), ItemCapped.class).switchIfEmpty(ServerResponse.notFound().build());

    }

}
