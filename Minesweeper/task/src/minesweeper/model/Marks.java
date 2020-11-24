package minesweeper.model;

public enum Marks {
    UNEXPLORED('.'), // for print only
    FREE('/'),
    MINE ('X'),
    UNEXPLORED_MARKED('*'); // for print only

    private final char ch;

    Marks(char ch) {
        this.ch = ch;
    }

    public char get() {
        return ch;
    }

    public static boolean isNumberMark(char mark) {
        return "12345678".indexOf(mark) != -1;
    }

    public boolean equals(char ch) {
        return this.ch == ch;
    }
}
