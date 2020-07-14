package com.rxspringboot.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {


    @Test
    public void backPressureTest1() {

        Flux<Integer> finiteFlux = Flux.range(1, 10).log();

        StepVerifier.create(finiteFlux)
                .thenRequest(1).expectNext(1)
                .thenRequest(1).expectNext(2)
                .thenCancel().verify();

    }

    @Test
    public void backPressureTest2() {

        Flux<Integer> finiteFlux = Flux.range(1, 10).log();


        finiteFlux.subscribe(
                element -> System.out.println("Element is: " + element),
                error -> System.err.println("Exception is: " + error),
                ()->System.out.println("Done"),
                s->s.request(3));
    }

    @Test
    public void backPressureTest3() {

        Flux<Integer> finiteFlux = Flux.range(1, 10).log();


        finiteFlux.subscribe(
                element -> System.out.println("Element is: " + element),
                error -> System.err.println("Exception is: " + error),
                ()->System.out.println("Done"),
                s->s.cancel());
    }

    @Test
    public void backPressureTest4() {

        Flux<Integer> finiteFlux = Flux.range(1, 10).log();


        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
               request(1);
                System.out.println("Value received is: "+ value);
                if (value==4) {
                    cancel();
                }
            }
        });
    }

}
