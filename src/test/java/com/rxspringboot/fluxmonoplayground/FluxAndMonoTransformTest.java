package com.rxspringboot.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {

    String[] data = new String[]{"spring", "boot", "rx"};


    @Test
    public void transformUsingMap() {

        Flux<String> flux = Flux.fromArray(data).map(s -> s.toUpperCase()).log();

        StepVerifier.create(flux).expectNext("SPRING", "BOOT", "RX").verifyComplete();

    }


    @Test
    public void transformUsingMapLength() {

        Flux<Integer> flux = Flux.fromArray(data).map(s -> s.length()).log();

        StepVerifier.create(flux).expectNext(6, 4, 2).verifyComplete();

    }

    @Test
    public void transformUsingMapLengthRepeat() {

        Flux<Integer> flux = Flux.fromArray(data).map(s -> s.length()).repeat(1).log();

        StepVerifier.create(flux).expectNext(6, 4, 2).expectNext(6, 4, 2).verifyComplete();

    }

    @Test
    public void transformUsingFilterAndMap() {

        Flux<String> flux = Flux.fromArray(data)
                .filter(s -> s.length() > 4)
                .log().map(s->s.toUpperCase());

        StepVerifier.create(flux).expectNext("SPRING").verifyComplete();

    }

    @Test
    public void transformUsingFlatMap() {

        Flux<String> flux = Flux.just("A", "B", "C", "D", "E", "F") // A, B, C, D, E, F
                .flatMap(s ->  {
                    return Flux.fromIterable(convertToList(s)); // A->List[A, newValue], B->List[B, newValue],
                }); //db or external service call that returns a flux -> s > flux<String>

        StepVerifier.create(flux.log()).expectNextCount(12).verifyComplete();

    }


    @Test
    public void transformUsingFlatMapParallel() {

        Flux<String> flux = Flux.just("A", "B", "C", "D", "E", "F") // A, B, C, D, E, F
                .window(2) //Flux<Flux<String> // (A, B), (C, D), (E, F)
                .flatMap(s ->
                    s.map(this::convertToList).subscribeOn(parallel())) // Flux<List<String>>
                    .flatMap (s -> Flux.fromIterable(s) //Flux<String>
                ); //db or external service call that returns a flux -> s > flux<String>

        StepVerifier.create(flux.log()).expectNextCount(12).verifyComplete();

    }

    @Test
    public void transformUsingFlatMapParallelKeepOrder() {

        Flux<String> flux = Flux.just("A", "B", "C", "D", "E", "F") // A, B, C, D, E, F
                .window(2) //Flux<Flux<String> // (A, B), (C, D), (E, F)
                .flatMapSequential(s ->
                        s.map(this::convertToList).subscribeOn(parallel())) // Flux<List<String>>
                .flatMap (s -> Flux.fromIterable(s) //Flux<String>
                ); //db or external service call that returns a flux -> s > flux<String>

        StepVerifier.create(flux.log()).expectNextCount(12).verifyComplete();

    }

    private List<String> convertToList(String s)  {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }


}
