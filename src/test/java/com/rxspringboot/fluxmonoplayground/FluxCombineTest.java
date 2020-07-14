package com.rxspringboot.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxCombineTest {

    @Test
    public  void fluxCombineMerge() {

        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> combinedFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(combinedFlux.log())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }

    @Test
    public  void fluxCombineMergeDelay() {

        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> combinedFlux = Flux.merge(flux1, flux2); //Interleaving

        StepVerifier.create(combinedFlux.log())
                .expectNextCount(6)
                .verifyComplete();

    }


    @Test
    public  void fluxCombineConcat() {

        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> combinedFlux = Flux.concat(flux1, flux2);

        StepVerifier.create(combinedFlux.log())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }

    @Test
    public  void fluxCombineConcatDelay() {

        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> combinedFlux = Flux.concat(flux1, flux2); //No interleaving

        StepVerifier.create(combinedFlux.log())
                .expectNextCount(6)
                .verifyComplete();

    }


    @Test
    public  void fluxCombineZip() {

        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> combinedFlux = Flux.zip(flux1, flux2, (t1, t2) ->{
            return t1.concat(t2); //AD, BE, CF
        });

        StepVerifier.create(combinedFlux.log())
                .expectNext("AD", "BE", "CF")
                .verifyComplete();

    }




}
