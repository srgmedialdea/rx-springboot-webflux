package com.rxspringboot.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class FluxMonoTest {

    @Test
    public  void fluxTest() {

        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                //.concatWith(Flux.error(RuntimeException::new))
                .concatWith(Flux.just("After error"))
                .log();

        stringFlux.subscribe(System.out::println, System.err::println, System.out::println);

    }


}
