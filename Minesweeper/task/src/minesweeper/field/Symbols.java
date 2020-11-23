package minesweeper.field;

public enum Symbols {
    SAFE ('.'),
    MINE ('X'),
    EMPTY_MARK('*'),
    MINE_MARK('x');

    private final char ch;

    Symbols(char ch) {
        this.ch = ch;
    }

    public char get() {
        return ch;
    }
}
