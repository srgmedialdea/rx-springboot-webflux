package com.rxspringboot.controller;

import com.rxspringboot.document.Item;
import com.rxspringboot.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("restrx/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepo;

    @GetMapping
    public Flux<Item> getAllItems() {
        return this.itemRepo.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Item>> getItem(@PathVariable String id) {
        return this.itemRepo.findById(id).map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> addItem(@RequestBody Item item) {
        return this.itemRepo.save(item);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItem(@PathVariable String id) {
        return this.itemRepo.findById(id).flatMap(item -> itemRepo.delete(item)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));

    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@RequestBody Item updatedItem, @PathVariable String id) {
        return this.itemRepo.findById(id).flatMap(item -> {
            updatedItem.setId(id);
            return itemRepo.save(updatedItem);
        }).map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

}
