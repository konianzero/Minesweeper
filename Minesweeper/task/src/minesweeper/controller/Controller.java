package minesweeper.controller;

import minesweeper.field.MineField;
import minesweeper.view.MineSweeperGame;

import static minesweeper.field.Symbols.*;

public class Controller {
    private MineSweeperGame game;
    private MineField field;

    public void setGame(MineSweeperGame game) {
        this.game = game;
    }

    public void setField(MineField field) {
        this.field = field;
    }

    public void onStart(int minesNum) {
        field.initField(minesNum);
        game.refresh(field);
    }

    public boolean markCell(int x, int y) {
        int r = convert(y);
        int c = convert(x);
        char mark = field.getCell(r, c);

        if (isNumberMark(mark)) {
            return false;
        } else if (isNotMarked(mark)) {
            field.markCell(r, c);
        } else if (isMarked(mark)) {
            field.unmarkCell(r, c);
        }

        game.refresh(field);
        return true;
    }

    private int convert(int i) {
        return i - 1;
    }

    private boolean isNumberMark(char mark) {
        return mark != SAFE.get() && mark != MINE.get() && mark != EMPTY_MARK.get() && mark != MINE_MARK.get();
    }

    private boolean isMarked(char mark) {
        return mark != SAFE.get() && mark != MINE.get();
    }

    private boolean isNotMarked(char mark) {
        return mark != EMPTY_MARK.get() && mark != MINE_MARK.get();
    }

    public boolean isOnlyMinesMarked() {
        return !field.isEmptyMarked() && field.allMinesMarked();
    }
}
