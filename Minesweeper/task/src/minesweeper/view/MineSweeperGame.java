package minesweeper.view;

import minesweeper.controller.Controller;
import minesweeper.field.MineField;

import java.util.Scanner;

public class MineSweeperGame {
    public static final String MINES_NUM = "How many mines do you want on the field? > ";
    public static final String SET_MARKS = "Set/delete mines marks (x and y coordinates): > ";
    public static final String CONGRATULATIONS = "Congratulations! You found all the mines!";

    public static final String IS_NUMBER = "There is a number here!";

    private Scanner scanner;
    private Controller controller;

    public MineSweeperGame(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void start() {
        System.out.print(MINES_NUM);
        int minesNum = scanner.nextInt();

        controller.onStart(minesNum);

        setMark();
    }

    private void setMark() {
        while (!controller.isOnlyMinesMarked()) {
            System.out.print(SET_MARKS);
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            if (!controller.markCell(x, y)) {
                System.out.println(IS_NUMBER);
            }
        }
        System.out.println(CONGRATULATIONS);
    }

    public void refresh(MineField mineField) {
        System.out.println(mineField);
    }
}
