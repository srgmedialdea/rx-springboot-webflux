package com.rxspringboot.initialiazer;

import com.rxspringboot.document.Categoria;
import com.rxspringboot.document.Item;
import com.rxspringboot.document.ItemCapped;
import com.rxspringboot.document.Producto;
import com.rxspringboot.repository.CategoriaRepository;
import com.rxspringboot.repository.ItemCappedRepository;
import com.rxspringboot.repository.ItemRepository;
import com.rxspringboot.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private ItemCappedRepository itemCappedRepo;

    @Autowired
    private CategoriaRepository categoriaRepo;

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private ReactiveMongoOperations reactiveMongoOps;


    @Override
    public void run(String... args) throws Exception {
        initialDataSetUp();
        //createItemCappedCollection();

    }

    private void createItemCappedCollection() {
        reactiveMongoOps.dropCollection(ItemCapped.class);
        reactiveMongoOps.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(10).capped());
        initialCappedDataSetUp();
    }

    public List<Item> getData() {
        return Arrays.asList(
                new com.rxspringboot.document.Item(null, "Samsung TV", 400.00),
                new com.rxspringboot.document.Item(null, "LG TV", 420.00),
                new com.rxspringboot.document.Item(null, "Apple Watch", 299.99),
                new com.rxspringboot.document.Item("xyz", "Fenix5 Watch", 99.99),
                new com.rxspringboot.document.Item("ABC", "Beats Headphones", 149.99));
    }

    private void initialDataSetUp() {
        itemRepo.deleteAll().thenMany(Flux.fromIterable(getData()))
                .flatMap(itemRepo::save)
                .thenMany(itemRepo.findAll())
                .subscribe(i -> log.info("Item inserted {}", String.valueOf(i)));


        reactiveMongoOps.dropCollection("productos").subscribe();
        reactiveMongoOps.dropCollection("categorias").subscribe();

        Categoria electronico = new Categoria("Electrónico");
        Categoria deporte = new Categoria("Deporte");
        Categoria computacion = new Categoria("Computación");
        Categoria muebles = new Categoria("Muebles");

        Flux.just(electronico, deporte, computacion, muebles)
                .flatMap(categoriaRepo::save)
                .doOnNext(c ->{
                    log.info("Categoria creada: " + c.getNombre() + ", Id: " + c.getId());
                }).thenMany(
                Flux.just(new Producto("TV Panasonic Pantalla LCD", 456.89, electronico),
                        new Producto("Sony Camara HD Digital", 177.89, electronico),
                        new Producto("Apple iPod", 46.89, electronico),
                        new Producto("Sony Notebook", 846.89, computacion),
                        new Producto("Hewlett Packard Multifuncional", 200.89, computacion),
                        new Producto("Bianchi Bicicleta", 70.89, deporte),
                        new Producto("HP Notebook Omen 17", 2500.89, computacion),
                        new Producto("Mica Cómoda 5 Cajones", 150.89, muebles),
                        new Producto("TV Sony Bravia OLED 4K Ultra HD", 2255.89, electronico)
                )
                        .flatMap(producto -> {
                            producto.setCreateAt(new Date());
                            return productoRepo.save(producto);
                        })).subscribe();


    }

    private void initialCappedDataSetUp() {
        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofSeconds(1))
                .map(i -> new ItemCapped(null, "RandomItem" + i, i.doubleValue()));
        itemCappedRepo.insert(itemCappedFlux).subscribe(itemCapped -> log.info("Inserted capped item is {}", itemCapped));
    }
}
