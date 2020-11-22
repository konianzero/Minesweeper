package minesweeper.field;

import java.util.Random;
import java.util.stream.IntStream;

import static minesweeper.field.Symbols.MINE;
import static minesweeper.field.Symbols.SAFE;

public class MineField {

    private static final int SIZE = 9;

    private final Random random = new Random();

    private char[][] field;

    public MineField() {
        fillBoard();
    }

    private void fillBoard() {
        field = new char[SIZE][SIZE];

        IntStream.range(0, SIZE)
                .forEach(row -> IntStream.range(0, SIZE)
                        .forEach(col -> field[row][col] = getRandomSymbol().getChar())
                );
    }

    private Symbols getRandomSymbol() {
        return random.nextInt(SIZE) == SIZE / 2 ? MINE : SAFE;
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
