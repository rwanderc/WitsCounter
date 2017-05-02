package com.wandercosta.witscounter.counter;

import static org.junit.Assert.assertEquals;

import com.wandercosta.witscounter.counter.WitsMatrixCounter.CountResult;
import org.junit.Test;

/**
 * Class to test WitsMatrixCounter.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
public class WitsMatrixCounterTest {

    @Test
    public void shouldAddLineAndCount() {
        WitsMatrixCounter counter = new WitsMatrixCounter();

        counter.add("01010");
        assertCounter(1, 1, counter.countAndClear());

        counter.add("01010");
        counter.add("01010");
        assertCounter(2, 1, counter.countAndClear());

        counter.add("01010");
        counter.add("01010");
        counter.add("01010");
        assertCounter(3, 1, counter.countAndClear());

        counter.add("01010");
        counter.add("01020");
        counter.add("01030");
        assertCounter(3, 3, counter.countAndClear());
    }

    @Test
    public void shouldAddLineWithStringValueAndCount() {
        WitsMatrixCounter counter = new WitsMatrixCounter();

        counter.add("01010");
        assertCounter(1, 1, counter.countAndClear());

        counter.add("01010");
        counter.add("0101StringTest");
        assertCounter(2, 1, counter.countAndClear());
    }

    @Test
    public void shouldAddBlockAndCount() {
        WitsMatrixCounter counter = new WitsMatrixCounter();

        counter.add("&&\n01010\n!!");
        assertCounter(1, 1, counter.countAndClear());

        counter.add("&&\n01010\n01020\n!!");
        assertCounter(2, 2, counter.countAndClear());

        counter.add("&&\n01010\n01020\n01030\n!!");
        assertCounter(3, 3, counter.countAndClear());
    }

    @Test
    public void shouldAddLineWithoutValue() {
        WitsMatrixCounter counter = new WitsMatrixCounter();

        counter.add("0101");
        assertCounter(0, 0, counter.countAndClear());
    }

    @Test
    public void shouldAddNullLine() {
        WitsMatrixCounter counter = new WitsMatrixCounter();

        counter.add(null);
        assertCounter(0, 0, counter.countAndClear());
    }

    @Test
    public void shouldAddEmptyLine() {
        WitsMatrixCounter counter = new WitsMatrixCounter();

        counter.add("");
        assertCounter(0, 0, counter.countAndClear());
    }

    @Test
    public void shouldAddNonWitsLine() {
        WitsMatrixCounter counter = new WitsMatrixCounter();

        counter.add("abcd");
        assertCounter(0, 0, counter.countAndClear());

        counter.add("010a0");
        assertCounter(0, 0, counter.countAndClear());

        counter.add("&&");
        assertCounter(0, 0, counter.countAndClear());

        counter.add("!!");
        assertCounter(0, 0, counter.countAndClear());
    }

    private void assertCounter(int total, int unique, CountResult count) {
        assertEquals(total, count.getTotal());
        assertEquals(unique, count.getUnique());
    }

}
