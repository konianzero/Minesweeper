package minesweeper.model;

import java.util.*;
import java.util.stream.IntStream;

import static minesweeper.model.Marks.*;

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
    private int shift;
    private int minesMarked;
    private int unexploredMarked;
    private char[][] field;
    private BitSet visibility;
    private BitSet marks;

    public void initField(int minesNum) {
        this.minesNum = minesNum;
        shift = (SIZE * SIZE) / minesNum;

        field = new char[SIZE][SIZE];

        fillField(UNEXPLORED);
        initMines();

        visibility = new BitSet(SIZE * SIZE);
        marks = new BitSet(SIZE * SIZE);
    }

    private void fillField(Marks symbol) {
        IntStream.range(0, SIZE)
                .forEach(row -> IntStream.range(0, SIZE)
                        .forEach(col -> field[row][col] = symbol.get())
                );
    }

    public void initMines() {
        placeRandomMines();
        setNumberOfMinesAround();
    }

    private void placeRandomMines() {
        Arrays.stream(randoms())
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
        if (UNEXPLORED.equals(field[row][col])) {
            int minesAround = countMinesAround(row, col);
            field[row][col] = minesAround == 0 ? FREE.get() : Character.forDigit(minesAround, 10);
        }
    }

    private int countMinesAround(int row, int col) {
        BitSet neighborCells = new BitSet();
        int neighborsCount = 0;
        for (int[] coords: neighborsCoordinates) {
            int r = coords[1] + row;
            int c = coords[0] + col;

            if (isOutOfBounds(r) || isOutOfBounds(c)) {
                continue;
            }

            if (MINE.equals(field[r][c])) {
                neighborCells.set(neighborsCount);
            }

            neighborsCount++;
        }
        return neighborCells.cardinality();
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

    public boolean isVisible(int row, int col) {
        return visibility.get(row * SIZE + col);
    }
    public boolean isMarked(int row, int col) {
        return marks.get(row * SIZE + col);
    }

    public void makeVisible(int row, int col) {
        setVisible(row, col);
    }

    public void exploreCell(int row, int col) {
        Deque<int[]> stack = new ArrayDeque<>();
        stack.offerFirst(new int[]{row, col});

        while (!stack.isEmpty()) {
            int[] cell = stack.pollFirst();
            int cRow = cell[0];
            int cCol = cell[1];
            setVisible(cRow, cCol);

            exploreNeighbors(stack, cRow, cCol);
        }
    }

    private void exploreNeighbors(Deque<int[]> stack, int cRow, int cCol) {
        for (int[] coords: neighborsCoordinates) {
            int nRow = coords[1] + cRow;
            int nCol = coords[0] + cCol;

            if (isOutOfBounds(nRow) || isOutOfBounds(nCol)) {
                continue;
            }

            char mark = field[nRow][nCol];

            if (!isVisible(nRow, nCol)) {
                if (FREE.equals(mark)) {
                    stack.offerFirst(new int[]{nRow, nCol});
                } else if (Marks.isNumberMark(mark)) {
                    setVisible(nRow, nCol);
                }
            } else if (isMarked(nRow, nCol) && !MINE.equals(mark)) {
                setUnmarked(nRow, nCol);
                /*
                    It's no need to recalculate number of mines around,
                    but without that the tests may not pass. Don't no why?
                 */
                int minesAround = countMinesAround(nRow, nCol);

                if (minesAround == 0) {
                    field[nRow][nCol] = FREE.get();
                    stack.offerFirst(new int[]{nRow, nCol});
                } else {
                    field[nRow][nCol] = Character.forDigit(minesAround, 10);
                }
            }
        }
    }

    private void setVisible(int row, int col) {
        visibility.set(row * SIZE + col, true);
    }

    private void setInvisible(int row, int col) {
        visibility.set(row * SIZE + col, false);
    }

    private void setMarked(int row, int col) {
        marks.set(row * SIZE + col, true);
    }

    private void setUnmarked(int row, int col) {
        marks.set(row * SIZE + col, false);
    }

    public void setMinesVisible() {
        IntStream.range(0, SIZE)
                .forEach(row -> IntStream.range(0, SIZE)
                        .forEach(col -> {
                            if (MINE.equals(field[row][col])) {
                                setVisible(row, col);
                            }
                        })
                );
    }

    public void markCell(int row, int col) {
        setVisible(row, col);
        setMarked(row, col);

        if (MINE.equals(field[row][col])) {
            minesMarked++;
        } else {
            unexploredMarked++;
        }
    }

    public void unmarkCell(int row, int col) {
        setInvisible(row, col);
        setUnmarked(row, col);

        if (MINE.equals(field[row][col])) {
            minesMarked--;
        } else {
            unexploredMarked--;
        }
    }

    public boolean allMinesMarked() {
         return minesNum == minesMarked;
    }

    public boolean hasUnexploredMarked() {
        return unexploredMarked > 0;
    }

    public boolean hasOpenedAllSafeCells() {
//        return IntStream.range(0, SIZE)
//                .mapToObj(row -> IntStream.range(0, SIZE)
//                        .filter(col -> (FREE.equals(field[row][col]) || Marks.isNumberMark(field[row][col]))
//                                        && isVisible(row, col)
//                        )
//                ).count() == (SIZE * SIZE) - minesNum;

        return visibility.cardinality() == (SIZE * SIZE) - minesNum;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(LS + COLUMN_HEADER + LS);
        result.append(LINE + LS);
        IntStream.range(0, SIZE)
                .forEach(row -> {
                            result.append(row + 1).append(WALL);
                            IntStream.range(0, SIZE)
                                    .forEach(col -> result.append(getMarkToPrint(row, col)));
                            result.append(WALL + LS);
                        }
                );
        result.append(LINE);
        return result.toString();
    }

    private char getMarkToPrint(int r, int c) {
        char mark = field[r][c];

        if (isVisible(r, c)) {
            if (isMarked(r, c)) {
                return UNEXPLORED_MARKED.get();
            }

            return mark;
        }
        return UNEXPLORED.get();
    }
}
