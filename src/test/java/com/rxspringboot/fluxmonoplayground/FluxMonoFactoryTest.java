package com.rxspringboot.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.function.Supplier;

public class FluxMonoFactoryTest {

    String[] data = new String[]{"Spring", "Spring Boot", "Reactive Spring"};

    @Test
    public void fluxUsingIterable() {
        Flux<String> dataFlux = Flux.fromIterable(Arrays.asList(data)).log();

        StepVerifier.create(dataFlux)
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray() {
        Flux<String> dataFlux = Flux.fromArray(data);

        StepVerifier.create(dataFlux.log())
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream() {
        Flux<String> dataFlux = Flux.fromStream(Arrays.asList(data).stream());

        StepVerifier.create(dataFlux.log())
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange() {
        Flux<Integer> dataFlux = Flux.range(1, 5);

        StepVerifier.create(dataFlux.log())
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }


    @Test
    public void monoUsingJustOrEmpty() {
        Mono<String> dataMono = Mono.justOrEmpty(null); //Mono.Empty();

        StepVerifier.create(dataMono.log())
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier() {

        Supplier<String> supplier = () -> "RxSpringBoot";

        Mono<String> mono = Mono.fromSupplier(supplier);

        StepVerifier.create(mono.log())
                .expectNext("RxSpringBoot")
                .verifyComplete();
    }

}
