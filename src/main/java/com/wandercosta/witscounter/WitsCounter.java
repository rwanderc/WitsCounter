package com.wandercosta.witscounter;

import java.io.IOException;
import java.util.Objects;
import javax.net.SocketFactory;

/**
 * A Wits client that connects to a Wits server through TCP and counts the number of parameters
 * being received per time interval.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
class WitsCounter {

    private static final String NULL_SOCKET_FACTORY = "SocketFactory must be provided.";
    private static final String NULL_HOST = "Host must be provided.";
    private static final String WRONG_PORT = "Port must be between 1 and 65535.";
    private static final String WRONG_INTERVAL = "Interval must be greater than 99ms.";

    private final SocketFactory socketFactory;
    private final String host;
    private final int port;
    private final int interval;
    private final IntCounter counter;

    private CountPrinter counterPrinter;
    private WitsReader witsReader;

    private transient boolean keepRunning;

    WitsCounter(SocketFactory socketFactory, String host, int port, int interval) {
        validate(socketFactory, host, port, interval);
        this.socketFactory = socketFactory;
        this.host = host;
        this.port = port;
        this.interval = interval;
        this.counter = new IntCounter();
    }

    void start() {
        if (keepRunning) {
            return;
        }

        keepRunning = true;

        Object semaphore = new Object();
        byte[][] dataMap = new byte[99][99];

        counterPrinter = new CountPrinter(counter, semaphore, interval, dataMap);
        TCPSocketClient client = new TCPSocketClient(socketFactory, host, port);
        witsReader = new WitsReader(client, counter, semaphore, dataMap);

        try {
            counterPrinter.start();
            witsReader.start();
        } catch (IOException ex) {
            System.err.println("Error to connect to server. Halting...");
            System.exit(1);
        }
    }

    void stop() {
        if (!keepRunning) {
            return;
        }
        keepRunning = false;

        if (witsReader != null) {
            witsReader.stop();
        }
        if (counterPrinter != null) {
            counterPrinter.stop();
        }
    }

    private void validate(SocketFactory socketFactory, String host, int port, int interval) {
        Objects.requireNonNull(socketFactory, NULL_SOCKET_FACTORY);
        Objects.requireNonNull(host, NULL_HOST);
        if (port < 1 || port > 65_535) {
            throw new IllegalArgumentException(WRONG_PORT);
        }
        if (interval < 100) {
            throw new IllegalArgumentException(WRONG_INTERVAL);
        }
    }

}
