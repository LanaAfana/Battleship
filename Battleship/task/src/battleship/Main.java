package battleship;

import java.util.Scanner;

public class Main {

    public static int placeShip(BattleField battleField, int step, boolean isRepeat) {
        if (!isRepeat)
            System.out.printf("Enter the coordinates of the %s:\n", BattleField.KindsOfShips.values()[step].name);
        try (Scanner scanner = new Scanner(System.in)) {
            String input = scanner.nextLine();
            int[] inputCoordinates = battleField.getCoordinates(input, step);
            if (inputCoordinates.length == 4) {
                battleField.addShip(inputCoordinates, step);
                battleField.output(false);
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void outputFields(Player[] players, int numPlayer) {

        for (int i = 0; i < players.length; i++) {
            if (i != numPlayer) {
                players[i].getField().output(true);
                System.out.println("---------------------");
            }
        }
        players[numPlayer].getField().output(false);
        System.out.println();
    }

    public static boolean isNotSbFullCount(Player[] players) {
        for(Player player : players) {
            if (player.getCount() == BattleField.KindsOfShips.cellsToHit()) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        int size = 10;
        Player[] players = {new Player(new BattleField(size)),
                new Player(new BattleField(size))};
        for (int i = 0; i < players.length; i++) {
            System.out.printf("Player %d, place your ships on the game field%n", i + 1);
            players[i].getField().output(false);
            int step = 0;
            boolean isRepeat = false;
            while (step < BattleField.KindsOfShips.values().length) {
                int tempStep = step;
                step += placeShip(players[i].getField(), step, isRepeat);
                isRepeat = tempStep == step;
            }
            BattleField.pressEnter();
        }

        System.out.println("The game starts!\n");

        int numPlayer = 0;
        int numOpponent = 1;
        do {
            outputFields(players, numPlayer);
            System.out.printf("Player %d, it's your turn:%n", numPlayer + 1);
            try (Scanner scanner = new Scanner(System.in)) {
                int[] inputCoordinate = players[numOpponent].getField().getCoordinate(scanner.nextLine());
                if (inputCoordinate.length == 2) {
                    players[numPlayer].addCount(players[numOpponent].getField().takeShot(inputCoordinate,
                            players[numPlayer].getCount()));
                }
            }
            numPlayer = numPlayer == 0 ? 1 : 0;
            numOpponent = numOpponent == 0 ? 1 : 0;

        } while (isNotSbFullCount(players));
    }
}
