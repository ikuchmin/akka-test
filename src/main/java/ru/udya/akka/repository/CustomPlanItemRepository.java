package ru.udya.akka.repository;

import akka.actor.AbstractActor;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ru.udya.akka.domain.CustomPlatItem;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CustomPlanItemRepository extends AbstractActor {

    public interface Command {}

    public static final class FindById implements CustomPlanItemRepository.Command {

        public final UUID customPlanItemId;
        public final ActorRef<OperationResult> replyTo;

        public FindById(UUID customPlanItemId, ActorRef<OperationResult> replyTo) {
            this.customPlanItemId = customPlanItemId;
            this.replyTo = replyTo;
        }
    }

    public static final class FindByIds implements CustomPlanItemRepository.Command {

        public final Collection<FindById> findByIds;

        public FindByIds(Collection<FindById> findByIds) {
            this.findByIds = findByIds;
        }
    }

    public interface OperationResult {}

    public static final class FoundCustomPlanItem implements OperationResult {

        public final CustomPlatItem customPlatItem;

        public FoundCustomPlanItem(CustomPlatItem customPlatItem) {
            this.customPlatItem = customPlatItem;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FindById.class, this::findById)
                .match(FindByIds.class, this::findByIds)
                .matchAny(el -> System.out.format("Got messages in actor: %s\n", el))
                .build();
    }

    private void findByIds(FindByIds command) {
        System.out.println("from findByIdS");
    }

    private void findById(FindById command) {
        System.out.println("from findById");
    }
}
