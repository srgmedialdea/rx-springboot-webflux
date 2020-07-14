package com.rxspringboot.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoFilterTest {

    String[] data = new String[]{"Spring", "Spring Boot", "Reactive Spring"};


    @Test
    public void filterTest() {

        Flux<String> flux = Flux.fromArray(data).filter(s->s.startsWith("S")).log();

        StepVerifier.create(flux).expectNext("Spring", "Spring Boot").verifyComplete();

    }

    @Test
    public void filterTestLength() {

        Flux<String> flux = Flux.fromArray(data).filter(s->s.length() > 6).log();

        StepVerifier.create(flux).expectNext("Spring Boot", "Reactive Spring").verifyComplete();

    }


}
