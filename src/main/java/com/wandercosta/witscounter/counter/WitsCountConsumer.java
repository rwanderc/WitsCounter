package com.wandercosta.witscounter.counter;

import java.util.Objects;

/**
 * A class that consumes the counter and prints its results.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
public class WitsCountConsumer {

    private static final String NULL_MATRIX = "WitsMatrixCounter must be provided.";
    private static final String WRONG_INTERVAL = "Interval must be greater than 100.";

    private final WitsMatrixCounter matrix;
    private final int interval;

    private transient boolean keepRunning;
    private Thread thread;

    public WitsCountConsumer(WitsMatrixCounter matrix, int interval) {
        validate(matrix, interval);
        this.matrix = matrix;
        this.interval = interval;
    }

    public synchronized void start() {
        if (keepRunning) {
            return;
        }

        keepRunning = true;
        thread = new Thread(this::startMethod);
        thread.start();
    }

    public void stop() {
        if (!keepRunning) {
            return;
        }
        keepRunning = false;
        thread.interrupt();
    }

    private void startMethod() {
        while (keepRunning) {
            try {
                Thread.sleep(interval - (System.currentTimeMillis() % interval));
            } catch (InterruptedException ex) {
                break;
            }
            
            WitsMatrixCounter.CountResult count = matrix.countAndClear();
            print(count.getTotal(), count.getUnique(), interval);
        }
    }

    private void print(int total, int unique, int interval) {
        System.out.println(total + " total var(s) / " + interval + " ms");
        System.out.println(unique + " unique var(s) / " + interval + " ms");
    }

    private void validate(WitsMatrixCounter matrix, int interval) {
        Objects.requireNonNull(matrix, NULL_MATRIX);
        if (interval < 100) {
            throw new IllegalArgumentException(WRONG_INTERVAL);
        }
    }

}
