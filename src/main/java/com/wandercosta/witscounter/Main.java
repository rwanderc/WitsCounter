package com.wandercosta.witscounter;

import com.wandercosta.witscounter.connection.TCPClient;
import com.wandercosta.witscounter.counter.WitsCountConsumer;
import com.wandercosta.witscounter.counter.WitsCountProducer;
import com.wandercosta.witscounter.counter.WitsMatrixCounter;
import java.io.IOException;
import java.util.Arrays;
import javax.net.SocketFactory;

/**
 * A command-line Wits client that connects to a Wits server through TCP and counts the number of
 * parameters being received per time interval.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
public class Main {

    public static void main(String[] args) {
        if (Arrays.asList(args).contains("--help")) {
            exit(false, help());
        } else if (args == null || args.length != 3) {
            exit(true, "Wrong input: 3 parameters are expected or --help.");
        }

        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            int interval = Integer.parseInt(args[2]);

            WitsMatrixCounter counter = new WitsMatrixCounter();
            SocketFactory socketFactory = SocketFactory.getDefault();
            TCPClient client = new TCPClient(socketFactory, host, port);
            WitsCountProducer producer = new WitsCountProducer(client, counter);
            WitsCountConsumer consumer = new WitsCountConsumer(counter, interval);
            new WitsCounter(consumer, producer).start();
        } catch (IOException ex) {
            exit(true, "Error to connect to server: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            exit(true, "Error reading parameters: " + ex.getMessage());
        }
    }

    private static void exit(boolean failure, String message) {
        System.out.println(message);
        System.exit(failure ? 1 : 0);
    }

    private static String help() {
        return "Usage: java -jar WitsCounter.jar [host] [port] [interval]\n"
                + "Connects to a Wits server through TCP Socket and prints the number\n"
                + "of parameters being received per time interval. It does not\n"
                + "reconnect if the connection fails.\n"
                + "  [host]\tThe IP of hostname of the server\n"
                + "  [port]\tThe port of the server\n"
                + "  [interval]\tThe time interval, in milliseconds, to aggregate\n"
                + "\t\tvalues. If less or equal 0, 1000 is used.";
    }

}
