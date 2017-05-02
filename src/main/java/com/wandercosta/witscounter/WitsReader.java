package com.wandercosta.witscounter;

import java.io.IOException;

/**
 * A Wits client that connects to a Wits server through TCP and counts the number of parameters
 * being received per time interval.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
class WitsReader {

    private final TCPSocketClient client;
    private final IntCounter counter;
    private final Object semaphore;
    private final byte[][] dataMap;

    private transient boolean keepRunning;

    WitsReader(TCPSocketClient client, IntCounter counter, Object semaphore, byte[][] dataMap) {
        this.client = client;
        this.counter = counter;
        this.semaphore = semaphore;
        this.dataMap = dataMap;
    }

    void start() throws IOException {
        if (keepRunning) {
            return;
        }

        keepRunning = true;
        readAndCount(semaphore, dataMap);
    }

    void stop() {
        if (!keepRunning) {
            return;
        }

        keepRunning = false;
    }

    private void readAndCount(Object semaphore, byte[][] dataMap) throws IOException {
        client.connect();

        String text = null;
        while (keepRunning) {
            try {
                text = client.readLine();
            } catch (IOException ex) {
            }

            if (text != null && !text.isEmpty()) {
                byte[][] map = WitsMapper.map(text);
                synchronized (semaphore) {
                    WitsMapper.add(dataMap, map);
                }
                int localCounter = WitsMapper.countTotal(map);

                counter.set(counter.get() + localCounter);
            }
        }

        client.disconnect();
    }

}
