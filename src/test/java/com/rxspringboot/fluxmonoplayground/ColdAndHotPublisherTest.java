package com.rxspringboot.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {

        Flux<String> data = Flux.just("A","B","C","D","E","F").delayElements(Duration.ofSeconds(1));

        data.subscribe(s -> System.out.println("Subscriber 1:" +s));
        Thread.sleep(2000);
        data.subscribe(s -> System.out.println("Subscriber 2:" +s));
        Thread.sleep(2000);

    }

    @Test
    public void hotPublisherTest() throws InterruptedException {

       Flux<String> data = Flux.just("A","B","C","D","E","F").delayElements(Duration.ofSeconds(1));

       ConnectableFlux<String> conn = data.publish();
       conn.connect();
       conn.subscribe(s -> System.out.println("Subscriber 1:" +s));
       Thread.sleep(3000);
       data.subscribe(s -> System.out.println("Subscriber 2:" +s));
       Thread.sleep(4000);

    }


}
