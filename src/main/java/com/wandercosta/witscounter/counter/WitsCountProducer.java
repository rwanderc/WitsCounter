package com.wandercosta.witscounter.counter;

import com.wandercosta.witscounter.connection.TCPClient;
import java.io.IOException;
import java.util.Objects;

/**
 * Produces the counts from the inputed matrixes of data.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
public class WitsCountProducer {

    private static final String NULL_CLIENT = "TCPClient must be provided.";
    private static final String NULL_MATRIX = "WitsCountMatrix must be provided.";

    private final TCPClient client;
    private final WitsMatrixCounter matrix;

    private transient boolean keepRunning;
    private Thread thread;

    public WitsCountProducer(TCPClient client, WitsMatrixCounter matrix) {
        validate(client, matrix);
        this.client = client;
        this.matrix = matrix;
    }

    public synchronized void start() throws IOException {
        if (keepRunning) {
            return;
        }
        keepRunning = true;
        client.connect();
        thread = new Thread(this::readAndCount);
        thread.start();
    }

    public synchronized void stop() {
        if (!keepRunning) {
            return;
        }
        keepRunning = false;
        thread.interrupt();
    }

    private void readAndCount() {
        String text = null;
        while (keepRunning) {
            try {
                text = client.readLine();
            } catch (IOException ex) {
            }

            matrix.add(text);
        }

        client.disconnect();
    }

    private void validate(TCPClient client, WitsMatrixCounter matrix) {
        Objects.requireNonNull(client, NULL_CLIENT);
        Objects.requireNonNull(matrix, NULL_MATRIX);
    }

}
