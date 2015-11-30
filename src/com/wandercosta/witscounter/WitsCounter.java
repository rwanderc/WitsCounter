package com.wandercosta.witscounter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * A command-line Wits client that connects to a Wits server through TCP and
 * counts the number of parameters being received per time interval.
 *
 * @author Roberto Wander (www.wandercosta.com)
 */
public class WitsCounter {

    private final Object counterSemaphore;
    private final boolean[][] map;
    private int totalCounter;

    public WitsCounter() {
        counterSemaphore = new Object();
        totalCounter = 0;
        map = new boolean[99][99];
    }

    public void start(String host, Integer port, Integer interval) {

        if (interval <= 0) {
            interval = 1000;
            System.out.println("Overwriting interval to 1000ms...");
        }

        try {

            Socket client = new Socket(host, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            new Thread() {

                @Override
                public void run() {

                    String text = null;

                    while (true) {

                        try {
                            text = reader.readLine();
                        } catch (Throwable ex) {
                        }

                        if (text != null) {
                            count(text);
                        }

                    }

                }

            }.start();

            long sleep = 1000 - (System.currentTimeMillis() % 1000);
            try {
                Thread.sleep(sleep);
            } catch (Throwable ex) {
            }

            int tempCounter;

            while (true) {

                synchronized (counterSemaphore) {
                    tempCounter = totalCounter;
                    totalCounter = 0;
                }

                System.out.println(tempCounter + " total var(s) / " + interval + " ms");
                System.out.println(countDifferentInMap(map) + " diff var(s) / " + interval + " ms");
                sleep = interval - (System.currentTimeMillis() % interval);
                try {
                    Thread.sleep(sleep);
                } catch (Throwable ex) {
                }

            }

        } catch (Throwable ex) {

            System.err.println("Unknown error. Halting...");
            System.exit(1);

        }

    }

    private void count(String data) {

        if (data.isEmpty()) {
            return;
        }

        int tmpCounter = 0;

        do {

            int linebreak = data.indexOf("\n");
            String line;

            if (linebreak == -1) {
                line = data;
                data = "";
            } else {
                line = data.substring(0, linebreak);
                data = data.substring(linebreak + 1, data.length());
            }

            if (line != null) {

                if (!line.startsWith("&&") && !line.startsWith("!!")) {

                    try {
                        int recIndex = Integer.valueOf(line.substring(0, 2));
                        int itemIndex = Integer.valueOf(line.substring(2, 4));
                        map[recIndex - 1][itemIndex - 1] = true;
                        tmpCounter++;
                    } catch (Throwable ex) {

                    }

                }

            }

        } while (!data.isEmpty());

        synchronized (counterSemaphore) {
            totalCounter += tmpCounter;
        }

    }

    /**
     * Main method to call application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        if (args == null || args.length == 0 || ("--help".compareTo(args[0]) == 0) || args.length != 3) {

            System.out.println("Usage: java -jar WitsCounter.jar [host] [port] [interval]");
            System.out.println(
                    "Connects to a Wits server through TCP Socket and prints the number\n"
                    + "of parameters being received per time interval.\n"
                    + "Does not reconnect if connection fails.\n"
                    + "  [host]\tThe IP of hostname of the server\n"
                    + "  [port]\tThe port of the server\n"
                    + "  [interval]\tThe time interval, in milliseconds, to aggregate\n"
                    + "\t\tvalues. If less or equal 0, 1000 is used.");
            System.exit(1);

        }

        String host = args[0];
        Integer port = null;
        Integer interval = null;

        try {

            port = Integer.valueOf(args[1]);
            interval = Integer.valueOf(args[2]);

        } catch (Throwable ex) {

            System.err.println("Need 2 parameters");
            System.exit(1);

        }

        WitsCounter counter = new WitsCounter();
        counter.start(host, port, interval);

    }

    private int countDifferentInMap(boolean[][] map) {

        int diffCounter = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != false) {
                    diffCounter++;
                    map[i][j] = false;
                }
            }
        }

        return diffCounter;

    }

}
