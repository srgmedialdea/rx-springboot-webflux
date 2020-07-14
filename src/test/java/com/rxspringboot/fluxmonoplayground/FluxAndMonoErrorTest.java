package com.rxspringboot.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoErrorTest {

    @Test
    public  void fluxErrorHandlingOnErrorResume() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume(e -> {
                    System.out.println("Exception is: " + e);
                    return Flux.just("default1", "default2");
                });

        StepVerifier.create(stringFlux.log())
                .expectNext("A", "B", "C")
                //.expectError(RuntimeException.class)
                .expectNext("default1", "default2")
                .verifyComplete();

    }


    @Test
    public  void fluxErrorHandlingOnErrorReturn() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default1");

        StepVerifier.create(stringFlux.log())
                .expectNext("A", "B", "C")
                //.expectError(RuntimeException.class)
                .expectNext("default1")
                .verifyComplete();

    }

    @Test
    public  void fluxErrorHandlingOnErrorMap() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e -> new CustomException(e));

        StepVerifier.create(stringFlux.log())
                .expectNext("A", "B", "C")
                //.expectError(RuntimeException.class)
                .expectError(CustomException.class)
                .verify();

    }


    @Test
    public  void fluxErrorHandlingOnErrorMapRetry() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e -> new CustomException(e)).retry(1);

        StepVerifier.create(stringFlux.log())
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                //.expectError(RuntimeException.class)
                .expectError(CustomException.class)
                .verify();

    }

}
