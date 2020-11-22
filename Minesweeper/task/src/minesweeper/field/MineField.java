package minesweeper.field;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;
import java.util.stream.IntStream;

import static minesweeper.field.Symbols.MINE;
import static minesweeper.field.Symbols.SAFE;

public class MineField {
    private static final int SIZE = 9;
    private static final int[][] neighborsCoordinates = new int[][] {
            {-1, -1}, {0, -1}, {1, -1},
            {-1,  0},          {1,  0},
            {-1,  1}, {0,  1}, {1,  1}
    };

    private final Random random = new Random();
    private final int minesNum;
    private final int shift;

    private char[][] field;

    public MineField(int minesNum) {
        this.minesNum = minesNum;
        shift = (SIZE * SIZE) / minesNum;
        initField();
        placeRandomMines();
        setNumberOfMinesAround();
    }

    private void initField() {
        field = new char[SIZE][SIZE];

        IntStream.range(0, SIZE)
                .forEach(row -> IntStream.range(0, SIZE)
                        .forEach(col -> field[row][col] = SAFE.get())
                );
    }

    private void placeRandomMines() {
        int[] randoms = randoms();

        Arrays.stream(randoms)
                .forEach(i -> field[i / SIZE][i % SIZE] = MINE.get());
    }

    private int[] randoms() {
        return IntStream.range(0, minesNum)
                        .map(this::calcRandom)
                        .toArray();
    }

    private int calcRandom(int i) {
        int lowerBound = shift * i;
        int upperBound = shift * i + shift;
        return random.nextInt(upperBound - lowerBound) + lowerBound;
    }

    private void setNumberOfMinesAround() {
        IntStream.range(0, SIZE)
                .forEach(row -> IntStream.range(0, SIZE)
                        .forEach(col -> setNumberOfMines(row, col))
                );
    }

    private void setNumberOfMines(int row, int col) {
        if (field[row][col] == SAFE.get()) {
            int minesAround = getNeighbors(row, col).cardinality();
            field[row][col] = minesAround == 0 ? SAFE.get() : Character.forDigit(minesAround, 10);
        }
    }

    private BitSet getNeighbors(int row, int col) {
        BitSet neighbors = new BitSet();
        int neighborsCount = 0;
        for (int[] coords: neighborsCoordinates) {
            int r = coords[0] + row;
            int c = coords[1] + col;

            if (isOutOfBounds(r) || isOutOfBounds(c)) {
                continue;
            }

            if (field[r][c] == MINE.get()) {
                neighbors.set(neighborsCount);
            }

            neighborsCount++;
        }
        return neighbors;
    }

    private boolean isOutOfBounds(int a) {
        if (a >= SIZE) {
            return true;
        } else {
            return a < 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        IntStream.range(0, SIZE)
                .forEach(row -> {
                            IntStream.range(0, SIZE)
                                    .forEach(col -> result.append(field[row][col]));
                            result.append("\n");
                        }
                );
        return result.toString();
    }
}
