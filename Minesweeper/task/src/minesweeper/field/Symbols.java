package minesweeper.field;

enum Symbols {
    SAFE ('.'),
    MINE ('X');

    private final char ch;

    Symbols(char ch) {
        this.ch = ch;
    }

    public char get() {
        return ch;
    }
}
