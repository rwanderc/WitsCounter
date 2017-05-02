package com.wandercosta.witscounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.util.Objects;
import javax.net.SocketFactory;

/**
 * Class to connect to a socket server and read lines from the stream.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
class TCPSocketClient {

    private static final String NULL_SOCKET_FACTORY = "SocketFactory must be provided.";
    private static final String NULL_HOST = "Host must be provided.";
    private static final String WRONG_PORT = "Port must be between 1 and 65535.";

    private final SocketFactory socketFactory;
    private final String host;
    private final int port;

    private BufferedReader bufferedReader;
    private boolean connected;

    TCPSocketClient(SocketFactory socketFactory, String host, int port) {
        validate(socketFactory, host, port);
        this.socketFactory = socketFactory;
        this.host = host;
        this.port = port;
    }

    synchronized void connect() throws IOException {
        if (connected) {
            return;
        }
        connected = true;

        Socket socket = socketFactory.createSocket(host, port);
        Reader reader = new InputStreamReader(socket.getInputStream());
        bufferedReader = new BufferedReader(reader);
    }

    synchronized void disconnect() {
        if (!connected) {
            return;
        }

        try {
            bufferedReader.close();
        } catch (IOException ex) {
        }
        connected = false;
    }

    String readLine() throws IOException {
        return bufferedReader.readLine();
    }

    private void validate(SocketFactory socketFactory, String host, int port) {
        Objects.requireNonNull(socketFactory, NULL_SOCKET_FACTORY);
        Objects.requireNonNull(host, NULL_HOST);
        if (port < 1 || port > 65_535) {
            throw new IllegalArgumentException(WRONG_PORT);
        }
    }

}
