package com.wandercosta.witscounter;

import java.util.Arrays;
import javax.net.SocketFactory;

/**
 * A command-line Wits client that connects to a Wits server through TCP and counts the number of
 * parameters being received per time interval.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
public class Main {

    private static String help() {
        return "Usage: java -jar WitsCounter.jar [host] [port] [interval]\n"
                + "Connects to a Wits server through TCP Socket and prints the number\n"
                + "of parameters being received per time interval.\n"
                + "Does not reconnect if connection fails.\n"
                + "  [host]\tThe IP of hostname of the server\n"
                + "  [port]\tThe port of the server\n"
                + "  [interval]\tThe time interval, in milliseconds, to aggregate\n"
                + "\t\tvalues. If less or equal 0, 1000 is used.";
    }

    public static void main(String[] args) {
        if (args == null || args.length == 0 || Arrays.asList(args).contains("--help")
                || args.length != 3) {
            System.out.println(help());
            System.exit(1);
        }

        try {
            SocketFactory socketFactory = SocketFactory.getDefault();
            String host = args[0];
            Integer port = Integer.valueOf(args[1]);
            Integer interval = Integer.valueOf(args[2]);
            new WitsCounter(socketFactory, host, port, interval).start();
        } catch (NumberFormatException ex) {
            System.err.println("Need 2 parameters");
            System.exit(1);
        }
    }

}
