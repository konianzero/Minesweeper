package minesweeper;

import minesweeper.controller.Controller;
import minesweeper.field.MineField;
import minesweeper.view.MineSweeperGame;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        MineSweeperGame game = new MineSweeperGame(scanner);
        Controller controller = new Controller();
        MineField mineField = new MineField();

        game.setController(controller);
        controller.setGame(game);
        controller.setField(mineField);

        game.start();
    }
}
