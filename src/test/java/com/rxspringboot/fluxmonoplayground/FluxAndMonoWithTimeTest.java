package com.rxspringboot.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {


    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100)).log();// starts from 0 -> ...

        infiniteFlux.subscribe(element -> System.out.println("Values is: " + element));

        Thread.sleep(3000);

    }

    @Test
    public void infiniteSequenceTake() {

        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(100)).take(3).log();// starts from 0 -> ...

        finiteFlux.subscribe(element -> System.out.println("Values is: " + element));

        StepVerifier.create(finiteFlux).expectNext(0L, 1L, 2L).verifyComplete();

    }

    @Test
    public void infiniteSequenceMap() {

        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(100))
                .delayElements(Duration.ofSeconds(1)).map(l -> l.intValue()).take(3).log();// starts from 0 -> ...

        finiteFlux.subscribe(element -> System.out.println("Values is: " + element));

        StepVerifier.create(finiteFlux).expectNext(0, 1, 2).verifyComplete();

    }

}
