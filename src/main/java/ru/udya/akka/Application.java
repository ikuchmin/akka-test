package ru.udya.akka;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import ru.udya.akka.repository.CustomPlanItemRepository;
import ru.udya.akka.repository.CustomPlanItemRepository.FindById;
import ru.udya.akka.repository.CustomPlanItemRepository.FindByIds;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Application {

    public static void main(String[] args) {

        var system = ActorSystem.create("akka-test");

        var customPlanItemRepository = system.actorOf(Props.create(CustomPlanItemRepository.class));
        var customPlanItemRepositorySink = Sink.<List<FindById>>actorRef(customPlanItemRepository, new StreamCompleted());

        var queue = Source.<FindById>queue(10, OverflowStrategy.backpressure())
                .map(m -> {System.out.format("Got message %s\n", m); return m;})
                .groupedWithin(100, Duration.ofMillis(100))
                .to(customPlanItemRepositorySink)
                .run(system);

        var source = Source.from(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
                .map(id -> new FindById(UUID.randomUUID(), null));

        source.map(queue::offer).runWith(Sink.ignore(),system);
    }

    static class StreamCompleted {}
}
