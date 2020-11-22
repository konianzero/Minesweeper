package minesweeper.view;

import minesweeper.field.MineField;

import java.util.Scanner;

public class MineSweeperGame {
    public static final String MINES_NUM = "How many mines do you want on the field? > ";

    private Scanner scanner;

    public MineSweeperGame(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        System.out.print(MINES_NUM);
        int minesNum = scanner.nextInt();

        MineField mineField = new MineField(minesNum);

        System.out.println(mineField);
    }
}
