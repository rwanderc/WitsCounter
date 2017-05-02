package com.wandercosta.witscounter.counter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Class to test WitsCountConsumer.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class WitsCountConsumerTest {

    private static final String NULL_MATRIX = "WitsMatrixCounter must be provided.";
    private static final String WRONG_INTERVAL = "Interval must be greater than 100.";
    private static final int DUMMY_INTERVAL = 200;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private WitsMatrixCounter matrix;

    @Test
    public void shouldStartRunTwiceAndStop() throws InterruptedException {
        when(matrix.countAndClear())
                .thenReturn(new WitsMatrixCounter.CountResult(1, 1))
                .thenReturn(new WitsMatrixCounter.CountResult(2, 2));

        WitsCountConsumer consumer = new WitsCountConsumer(matrix, DUMMY_INTERVAL);
        consumer.start();
        sleepCycles(2);
        consumer.stop();

        verify(matrix, times(2)).countAndClear();
    }

    @Test
    public void shouldStartAndStopTwiceAndNotAffectResult() throws InterruptedException {
        when(matrix.countAndClear()).thenReturn(new WitsMatrixCounter.CountResult(1, 1));

        WitsCountConsumer consumer = new WitsCountConsumer(matrix, DUMMY_INTERVAL);
        consumer.start();
        consumer.start();
        sleepCycles(1);
        consumer.stop();
        consumer.stop();

        verify(matrix).countAndClear();
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void shouldInstantiateWithNullMatrix() {
        exception.expect(NullPointerException.class);
        exception.expectMessage(NULL_MATRIX);
        new WitsCountConsumer(null, DUMMY_INTERVAL);
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void shouldInstantiateWithWrongIntervalNegative1() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(WRONG_INTERVAL);
        new WitsCountConsumer(matrix, -1);
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void shouldInstantiateWithWrongInterval0() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(WRONG_INTERVAL);
        new WitsCountConsumer(matrix, 0);
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void shouldInstantiateWithWrongInterval99() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(WRONG_INTERVAL);
        new WitsCountConsumer(matrix, 99);
    }

    private void sleepCycles(int cycles) throws InterruptedException {
        if (cycles < 1) {
            throw new IllegalArgumentException("At least 1 cycle is required.");
        }
        // Sleeps n cycles and ends a little before the next, so as to prevent early executions
        long now = System.currentTimeMillis();
        Thread.sleep((long) ((cycles * 1.1 * DUMMY_INTERVAL) - now % DUMMY_INTERVAL));
    }

}
