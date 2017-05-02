package com.wandercosta.witscounter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Class to test IntCounter.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
public class IntCounterTest {

    @Test
    public void shouldGetSetAndGet() {
        IntCounter counter = new IntCounter();

        assertEquals(0, counter.get());

        counter.set(1);
        assertEquals(1, counter.get());

        counter.set(9);
        assertEquals(9, counter.get());

        counter.set(1119);
        assertEquals(1119, counter.get());
    }

}
