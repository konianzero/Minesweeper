package minesweeper.view;

import minesweeper.controller.Controller;
import minesweeper.model.MineField;

import java.util.Scanner;

public class MineSweeperGame {
    public static final String MINES_NUM = "How many mines do you want on the field? > ";
    public static final String SET_MARKS = "Set/unset mines marks or claim a cell as free: > ";
    public static final String CONGRATULATIONS = "Congratulations! You found all the mines!";

    public static final String FAILED = "You stepped on a mine and failed!";

    private final Scanner scanner;

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

        settingMarks();
    }

    private void settingMarks() {
        while (!controller.allMinesFound()) {
            System.out.print(SET_MARKS);
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            String s = scanner.next();

            if (!controller.processCell(x, y, s)) {
                System.out.println(FAILED);
                return;
            }
        }
        System.out.println(CONGRATULATIONS);
    }

    public void refresh(MineField mineField) {
        System.out.println(mineField);
    }
}
