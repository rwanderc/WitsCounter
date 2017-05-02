package com.wandercosta.witscounter;

/**
 * The class to provide capability to count Wits lines.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
class CountPrinter {

    private final IntCounter counter;
    private final Object semaphore;
    private final int interval;
    private final byte[][] dataMap;

    private boolean keepRunning;
    private Thread thread;

    CountPrinter(IntCounter counter, Object semaphore, int interval, byte[][] dataMap) {
        this.counter = counter;
        this.semaphore = semaphore;
        this.interval = interval;
        this.dataMap = dataMap;
    }

    void start() {
        if (keepRunning) {
            return;
        }

        keepRunning = true;
        thread = new Thread(this::startMethod);
        thread.start();
    }

    private void startMethod() {
        int tempCounter;
        while (keepRunning) {
            synchronized (semaphore) {
                tempCounter = counter.get();
                counter.set(0);
            }

            System.out.println(tempCounter + " total var(s) / " + interval + " ms");
            System.out.println(WitsMapper.countUnique(dataMap) + " unique var(s) / "
                    + interval + " ms");
            try {
                Thread.sleep(interval - (System.currentTimeMillis() % interval));
            } catch (InterruptedException ex) {
                break;
            }
        }
    }

    void stop() {
        if (!keepRunning) {
            return;
        }
        keepRunning = false;
        thread.interrupt();
    }

}
