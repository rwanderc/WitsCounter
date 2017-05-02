package com.wandercosta.witscounter;

import static org.mockito.Mockito.verify;

import com.wandercosta.witscounter.counter.WitsCountConsumer;
import com.wandercosta.witscounter.counter.WitsCountProducer;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Class to test WitsCounter.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class WitsCounterTest {

    private static final String NULL_CONSUMER = "WitsCountConsumer must be provided.";
    private static final String NULL_PRODUCER = "WitsCountProducer must be provided.";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private WitsCountConsumer consumer;

    @Mock
    private WitsCountProducer producer;

    @Test
    public void shouldStartAndStop() throws IOException {
        WitsCounter counter = new WitsCounter(consumer, producer);

        counter.start();
        verify(consumer).start();
        verify(producer).start();

        counter.stop();
        verify(consumer).stop();
        verify(producer).stop();
    }

    @Test
    public void shouldStartAndStopTwiceAndNotAffectResult() throws IOException {
        WitsCounter counter = new WitsCounter(consumer, producer);

        counter.start();
        counter.start();
        verify(consumer).start();
        verify(producer).start();

        counter.stop();
        counter.stop();
        verify(consumer).stop();
        verify(producer).stop();
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void shouldFailToInstantiateWithNullConsumer() {
        exception.expect(NullPointerException.class);
        exception.expectMessage(NULL_CONSUMER);
        new WitsCounter(null, producer);
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void shouldFailToInstantiateWithNullProducer() {
        exception.expect(NullPointerException.class);
        exception.expectMessage(NULL_PRODUCER);
        new WitsCounter(consumer, null);
    }

}
