package com.wandercosta.witscounter.counter;

import java.util.Arrays;

/**
 * Class with synchronized methods to provide capability to count lines in of Wits streaming.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
public class WitsMatrixCounter {

    private static final int SIZE = 99;

    private final byte[][] matrix;

    public WitsMatrixCounter() {
        this.matrix = new byte[SIZE][SIZE];
    }

    /**
     * Counts the lines of Wits data from a block or line.
     *
     * @param wits the block or line of Wits data.
     */
    synchronized void add(String wits) {
        if (wits != null && !wits.isEmpty()) {
            String[] lines = wits.split("\n");
            Arrays.stream(lines).filter(this::filterLine).forEach(this::addLine);
        }
    }

    private boolean filterLine(String line) {
        return line != null
                && line.length() > 4
                && !line.startsWith("&&")
                && !line.startsWith("!!");
    }

    private void addLine(String line) {
        try {
            int record = Integer.parseInt(line.substring(0, 2));
            int item = Integer.parseInt(line.substring(2, 4));
            matrix[record - 1][item - 1]++;
        } catch (NumberFormatException ex) {
            // Ignore non-wits lines
        }
    }

    /**
     * Returns the total and unique counters of Wits lines registered since last cleanup.
     *
     * @return an object containing the total and unique results counted.
     */
    synchronized CountResult countAndClear() {
        int total = 0;
        int unique = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (matrix[i][j] != 0) {
                    total += matrix[i][j];
                    unique++;
                    matrix[i][j] = 0;
                }
            }
        }
        return new CountResult(total, unique);
    }

    static class CountResult {

        private final int total;
        private final int unique;

        CountResult(int total, int unique) {
            this.total = total;
            this.unique = unique;
        }

        int getTotal() {
            return total;
        }

        int getUnique() {
            return unique;
        }

    }

}
