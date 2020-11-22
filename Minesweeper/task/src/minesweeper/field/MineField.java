package minesweeper.field;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static minesweeper.field.Symbols.MINE;
import static minesweeper.field.Symbols.SAFE;

public class MineField {

    private static final int SIZE = 9;

    private final Random random = new Random();
    private final int minesNum;
    private final int shift;

    private char[][] field;

    public MineField(int minesNum) {
        this.minesNum = minesNum;
        shift = (SIZE * SIZE) / minesNum;
        initField();
        setRandomMines();
    }

    private void initField() {
        field = new char[SIZE][SIZE];

        IntStream.range(0, SIZE)
                .forEach(row -> IntStream.range(0, SIZE)
                        .forEach(col -> field[row][col] = SAFE.get())
                );
    }

    private void setRandomMines() {
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
