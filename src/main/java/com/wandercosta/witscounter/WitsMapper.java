package com.wandercosta.witscounter;

/**
 * Class to count lines in the text.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
final class WitsMapper {

    private static final String DIFF_DIMENSIONS = "The matrixes have different sizes dimensions.";

    private WitsMapper() {
    }

    static byte[][] map(String data) {
        byte[][] map = new byte[99][99];

        if (data != null && !data.isEmpty()) {
            do {
                int linebreak = data.indexOf('\n');
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
                            int recIndex = Integer.parseInt(line.substring(0, 2));
                            int itemIndex = Integer.parseInt(line.substring(2, 4));
                            map[recIndex - 1][itemIndex - 1]++;
                        } catch (NumberFormatException ex) {
                        }
                    }
                }
            } while (!data.isEmpty());
        }
        return map;
    }

    static void add(byte[][] dest, byte[][] orig) {
        validateMatrixesCompatibility(dest, orig);

        for (int i = 0; i < dest.length; i++) {
            for (int j = 0; j < dest[i].length; j++) {
                dest[i][j] += orig[i][j];
            }
        }
    }

    private static void validateMatrixesCompatibility(byte[][] dest, byte[][] orig) {
        if (dest.length != orig.length || dest[0].length != orig[0].length) {
            throw new IllegalArgumentException(DIFF_DIMENSIONS);
        }
    }

    static int countTotal(byte[][] map) {
        int counter = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != 0) {
                    counter += map[i][j];
                }
            }
        }
        return counter;
    }

    static int countUnique(byte[][] map) {
        int counter = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != 0) {
                    counter++;
                }
            }
        }
        return counter;
    }

}
