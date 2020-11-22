package minesweeper;

import minesweeper.field.MineField;
import minesweeper.view.MineSweeperGame;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        MineSweeperGame game = new MineSweeperGame(scanner);
        game.start();
    }
}
