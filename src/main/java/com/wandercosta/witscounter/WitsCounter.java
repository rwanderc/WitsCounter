package com.wandercosta.witscounter;

import com.wandercosta.witscounter.counter.WitsCountConsumer;
import com.wandercosta.witscounter.counter.WitsCountProducer;
import java.io.IOException;
import java.util.Objects;

/**
 * The class that orchestrates the count producer and the count consumer.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
class WitsCounter {

    private static final String NULL_CONSUMER = "WitsCountConsumer must be provided.";
    private static final String NULL_PRODUCER = "WitsCountProducer must be provided.";

    private final WitsCountConsumer consumer;
    private final WitsCountProducer producer;

    private transient boolean keepRunning;

    WitsCounter(WitsCountConsumer consumer, WitsCountProducer producer) {
        validate(consumer, producer);
        this.consumer = consumer;
        this.producer = producer;
    }

    void start() throws IOException {
        if (keepRunning) {
            return;
        }

        keepRunning = true;
        consumer.start();
        producer.start();
    }

    void stop() {
        if (!keepRunning) {
            return;
        }
        keepRunning = false;

        if (producer != null) {
            producer.stop();
        }
        if (consumer != null) {
            consumer.stop();
        }
    }

    private void validate(WitsCountConsumer consumer, WitsCountProducer producer) {
        Objects.requireNonNull(consumer, NULL_CONSUMER);
        Objects.requireNonNull(producer, NULL_PRODUCER);
    }

}
