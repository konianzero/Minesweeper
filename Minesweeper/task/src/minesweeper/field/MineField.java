package minesweeper.field;

import java.util.*;
import java.util.stream.IntStream;

import static minesweeper.field.Symbols.*;

public class MineField {
    private final static String COLUMN_HEADER = " │123456789│";
    private final static String LINE = "—│—————————│";
    private final static String WALL = "|";
    private final static String LS = "\n";

    private static final int SIZE = 9;
    private static final int[][] neighborsCoordinates = new int[][] {
            {-1, -1}, {0, -1}, {1, -1},
            {-1,  0},          {1,  0},
            {-1,  1}, {0,  1}, {1,  1}
    };

    private final Random random = new Random();

    private int minesNum;
    private int minesMarked;
    private int emptyMarked;
    private int shift;
    private char[][] field;

    public void initField(int minesNum) {
        this.minesNum = minesNum;
        shift = (SIZE * SIZE) / minesNum;
        fillField(SAFE);
        placeRandomMines();
        setNumberOfMinesAround();
    }

    private void fillField(Symbols symbol) {
        field = new char[SIZE][SIZE];

        IntStream.range(0, SIZE)
                .forEach(row -> IntStream.range(0, SIZE)
                        .forEach(col -> field[row][col] = symbol.get())
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

    public char getCell(int row, int col) {
        return field[row][col];
    }

    public void markCell(int row, int col) {
        if (field[row][col] == MINE.get()) {
            minesMarked++;
            field[row][col] = MINE_MARK.get();
        } else if (field[row][col] == SAFE.get()) {
            emptyMarked++;
            field[row][col] = EMPTY_MARK.get();
        }
    }

    public void unmarkCell(int row, int col) {
        if (field[row][col] == EMPTY_MARK.get()) {
            emptyMarked--;
            field[row][col] = SAFE.get();
        } else if (field[row][col] == MINE_MARK.get()) {
            minesMarked--;
            field[row][col] = MINE.get();
        }
    }

    public boolean allMinesMarked() {
         return minesNum == minesMarked;
    }

    public boolean isEmptyMarked() {
        return emptyMarked > 0;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(LS + COLUMN_HEADER + LS);
        result.append(LINE + LS);
        IntStream.range(0, SIZE)
                .forEach(row -> {
                            result.append(row + 1).append(WALL);
                            IntStream.range(0, SIZE)
                                    .forEach(col -> result.append(getMark(row, col)));
                            result.append(WALL + LS);
                        }
                );
        result.append(LINE);
        return result.toString();
    }

    private char getMark(int r, int c) {
        char mark = field[r][c];

        if (mark == MINE.get()) {
            return SAFE.get();
        } else if (mark == MINE_MARK.get()) {
            return EMPTY_MARK.get();
        }
        return mark;
    }
}
