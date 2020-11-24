package minesweeper.controller;

import minesweeper.model.Marks;
import minesweeper.model.MineField;
import minesweeper.view.MineSweeperGame;

import static minesweeper.model.Marks.*;

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

    public boolean processCell(int x, int y, String s) {
        int r = convert(y);
        int c = convert(x);

        switch (s) {
            case "free":
                if (!explore(r, c)) {
                    game.refresh(field);
                    return false;
                }
                break;
            case "mine":
                mark(r, c);
                break;
        }

        game.refresh(field);
        return true;
    }

    private int convert(int i) {
        return i - 1;
    }

    private boolean explore(int r, int c) {
        char mark = field.getCell(r, c);

        if (Marks.isNumberMark(mark)) {
            field.makeVisible(r, c);
        } else if (FREE.equals(mark)) {
            field.exploreCell(r, c);
        } else if (MINE.equals(mark)) {
            field.setMinesVisible();
            return false;
        }
        return true;
    }

    private void mark(int r, int c) {
        if (field.isMarked(r, c)) {
            field.unmarkCell(r, c);
        } else {
            field.markCell(r, c);
        }
    }

    public boolean allMinesFound() {
        return (!field.hasUnexploredMarked() && field.allMinesMarked())
                || field.hasOpenedAllSafeCells();
    }
}
