package com.rxspringboot.controller;

import com.rxspringboot.document.ItemCapped;
import com.rxspringboot.repository.ItemCappedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("stream/items")
public class ItemStreamController {

    @Autowired
    private ItemCappedRepository itemCappedRepo;


    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemCapped> getItemStream() {
        return this.itemCappedRepo.findItemsBy();
    }
}
