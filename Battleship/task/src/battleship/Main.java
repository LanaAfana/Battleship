package battleship;

import java.util.Scanner;

public class Main {

    public static int placeShip(BattleField battleField, int step, boolean isRepeat) {
        if (!isRepeat)
            System.out.printf("Enter the coordinates of the %s:\n", BattleField.KindsOfShips.values()[step].name);
        try (Scanner scanner = new Scanner(System.in)) {
            int[] inputCoordinates = battleField.getCoordinates(scanner.nextLine(), step);
            if (inputCoordinates.length == 4) {
                battleField.addShip(inputCoordinates, step);
                battleField.output(false);
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void main(String[] args) {
        BattleField battleField = new BattleField(10);
        battleField.output(false);
        int step = 0;
        boolean isRepeat = false;
        while (step < BattleField.KindsOfShips.values().length) {
            int tempStep = step;
            step += placeShip(battleField, step, isRepeat);
            isRepeat = tempStep == step;
        }

        System.out.println("The game starts!");
        battleField.output(true);
        System.out.println("Take a shot!");
        int count = 0;
        while (count < BattleField.KindsOfShips.cellsToHit()) {
            try (Scanner scanner = new Scanner(System.in)) {
                int[] inputCoordinate = battleField.getCoordinate(scanner.nextLine());
                if (inputCoordinate.length == 2) {
                    count += battleField.takeShot(inputCoordinate, count);
                }
            }
        }
    }
}
