package com.rxspringboot.repository;

import com.rxspringboot.document.Categoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoriaRepository extends ReactiveMongoRepository<Categoria, String> {

}
