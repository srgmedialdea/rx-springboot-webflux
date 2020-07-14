package com.rxspringboot.repository;

import com.rxspringboot.document.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductoRepository extends ReactiveMongoRepository<Producto, String> {

}

