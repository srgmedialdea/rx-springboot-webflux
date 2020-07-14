package com.rxspringboot.repository;

import com.rxspringboot.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {


    Mono<Item> findByDescription(String description);
}
