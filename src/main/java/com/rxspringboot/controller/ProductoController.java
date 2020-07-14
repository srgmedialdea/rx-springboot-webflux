package com.rxspringboot.controller;


import com.rxspringboot.document.Producto;
import com.rxspringboot.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("productos")
public class ProductoController {


    @Autowired
    private ProductoRepository productoRepo;

    @GetMapping
    public Mono<ResponseEntity<Flux<Producto>>> getProductos() {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(productoRepo.findAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<Producto>>> getProducto(@PathVariable String id) {

        return  productoRepo.findById(id).map(producto ->
                ResponseEntity.created(URI.create("productos/".concat(id))).contentType(MediaType.APPLICATION_JSON).body(Mono.just(producto)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}

