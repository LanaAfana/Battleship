package battleship;


import java.util.Arrays;
import java.util.Scanner;

public class BattleField {
     private final String[][] field;
     private final int size;
     int[][] shipCrdnts;
     private static final String BUSY = "O";

     BattleField(int size) {
          this.size = size;
          field = new String[size][size];
          for (int i = 0; i < size; i++)
               for (int j = 0; j < size; j++) {
                    field[i][j] = "~";
               }
          shipCrdnts = new int[KindsOfShips.values().length][2];
     }

     enum KindsOfShips {
          AIRCRAFT(5, "Aircraft Carrier (5 cells)"),
          BATTLESHIP(4, "Battleship (4 cells)"),
          SUBMARINE(3, "Submarine (3 cells)"),
          CRUISER(3, "Cruiser (3 cells)"),
          DESTROYER(2, "Destroyer (2 cells)");

          private final int numOfCells;
          final String name;

          KindsOfShips(int numOfCells, String name) {
               this.numOfCells = numOfCells;
               this.name = name;
          }

          static int cellsToHit() {
               int sum = 0;
               for (KindsOfShips value : KindsOfShips.values()) {
                    sum += value.numOfCells;
               }
               return sum;
          }

     }

     public static char digitToLetter(int l) {
          return (char) (l + 65);
     }

     public static int letterToDigit(char l) {
          return (int) l - 65;
     }

     private static String getRawX(String input) {
          return input.split(" ")[0];
     }

     private static String getRawY(String input) {
          return input.split(" ")[1];
     }

     public static void pressEnter() {
          System.out.println("Press Enter and pass the move to another player\n");
          try (Scanner scanner = new Scanner(System.in)) {
               scanner.nextLine();
          }
     }

     private boolean isNotNumber(int number) {
          return (number < 0 || number >= this.size);
     }

     private boolean isCorrectLength(int step, int[] crdnts) {
          int length = (crdnts[0] == crdnts[2]) ? Math.abs(crdnts[1] - crdnts[3]) + 1:
                  Math.abs(crdnts[0] - crdnts[2]) + 1;
          return length == KindsOfShips.values()[step].numOfCells;
     }

     private boolean isCorrectLocation(int[] crdnts) {
          return (crdnts[0] != crdnts[2] || crdnts[1] != crdnts[3]) &&
                  (crdnts[1] == crdnts[3] || crdnts[0] == crdnts[2]);
     }

     private boolean isCloseTo(int[] crdnts) {
          int i1 = (crdnts[0] == 0) ? 0 : crdnts[0] - 1;
          int i2 = (crdnts[2] == field.length - 1) ? crdnts[2] : crdnts[2] + 1;
          int j1 = (crdnts[1] == 0) ? 0 : crdnts[1] - 1;
          int j2 = (crdnts[3] == field.length - 1) ? crdnts[3] : crdnts[3] + 1;

          if (crdnts[0] == crdnts[2]) {
               for (int j = j1; j <= j2; j++) {
                    if (field[crdnts[0]][j].equals(BUSY)) return true;
               }
               for (int i = i1; i <= i2; i++)
                    for (int j = crdnts[1]; j <= crdnts[3]; j++) {
                         if (field[i][j].equals(BUSY)) return true;
                    }
          } else {
               for (int i= i1; i <= i2; i++) {
                    if (field[i][crdnts[1]].equals(BUSY)) return true;
               }
               for (int i = crdnts[0]; i <= crdnts[2]; i++)
                    for (int j = j1; j <= j2; j++) {
                         if (field[i][j].equals(BUSY)) return true;
                    }
          }
          return false;
     }

     int[] getCoordinates(String input, int step) {
          int[] result = new int[4];
          String numPart;
          try {
               String x = getRawX(input);
               String y = getRawY(input);
               numPart = (x.length() == 2) ? String.valueOf(x.charAt(1)) :
                       x.charAt(1) + String.valueOf(x.charAt(2));
               int c1 = Integer.parseInt(numPart) - 1;
               numPart = (y.length() == 2) ? String.valueOf(y.charAt(1)) :
                       y.charAt(1) + String.valueOf(y.charAt(2));
               int c2 = Integer.parseInt(numPart) - 1;
               result[0] = Math.min(c1, c2);
               result[2] = Math.max(c1, c2);
               c1 = letterToDigit(x.charAt(0));
               c2 = letterToDigit(y.charAt(0));
               result[1] = Math.min(c1, c2);
               result[3] = Math.max(c1, c2);
          } catch (ArrayIndexOutOfBoundsException | NumberFormatException aio) {
               System.out.println("Error! Invalid input. Try again:");
          }
          return isCoordinates(result, step) ? result : new int[1];
     }

     int[] getCoordinate(String input) {
          int[] result = new int[2];
          try {
               String numPart = (input.length() == 2) ? String.valueOf(input.charAt(1)) :
                       input.charAt(1) + String.valueOf(input.charAt(2));
               result[0] = Integer.parseInt(numPart) - 1;
               result[1] = letterToDigit(input.charAt(0));
          } catch (NumberFormatException nfe) {
               System.out.println("Error! You entered the wrong coordinates! Try again:");
          }
          return isCoordinate(result) ? result : new int[1];
     }

     public boolean isCoordinates(int[] crdnts, int step) {
          if (isNotNumber(crdnts[1]) || isNotNumber(crdnts[0]) || isNotNumber(crdnts[3]) || isNotNumber(crdnts[2])) {
               System.out.println("Error! Invalid input. Try again:");
               return false;
          }
          if (!isCorrectLocation(crdnts)) {
               System.out.println("Error! Wrong ship location! Try again:");
               return false;
          }
          if (!isCorrectLength(step, crdnts)) {
               System.out.printf("Error! Wrong length of the %s! Try again:%n",
                       KindsOfShips.values()[step].name.split("\\(")[0].trim());
               return false;
          }
          if (isCloseTo(crdnts)) {
               System.out.println("Error! You placed it too close to another one. Try again:");
               return false;
          }
          return true;
     }

     public boolean isCoordinate(int[] crdnts) {
          if (isNotNumber(crdnts[0]) || isNotNumber(crdnts[1])) {
               System.out.println("Error! You entered the wrong coordinates! Try again:");
               return false;
          }
          return true;
     }

     public void output(boolean fog) {
          StringBuilder output = new StringBuilder("  ");

          for (int i = 1; i <= size; i++) {
               output.append(i).append(" ");
          }
          output.append("\n");
          for (int i = 0; i < size; i++) {
               for (int j = 0; j < size; j++) {
                    if (j == 0) {
                         output.append(digitToLetter(i)).append(" ");
                    }
                    if (fog && !field[j][i].equals("~")) {
                         output.append("~").append(" ");
                    } else {
                         output.append(field[j][i]).append(" ");
                    }
               }
               output.append("\n");
          }
          System.out.print(output);
     }

     public void addShip(int[] crdnts, int step) {
          shipCrdnts[step] = crdnts;
          for (int i = crdnts[0]; i <= crdnts[2]; i++){
               for (int j = crdnts[1]; j <= crdnts[3]; j++) {
                    this.field[i][j] = BUSY;
               }
          }
     }

     boolean isSankShip() {
          boolean flag;

          for (int[] shipCrdnt : shipCrdnts) {
               if (Arrays.stream(shipCrdnt).sum() == 0) continue;
               flag = true;
               if (shipCrdnt[0] == shipCrdnt[2]) { // x
                    for (int k = shipCrdnt[1]; k <= shipCrdnt[3]; k++) {
                         System.out.println("/ " + k);
                         if (!field[shipCrdnt[0]][k].equals("X")) {
                              flag = false;
                         }
                    }
               } else {
                    for (int k = shipCrdnt[0]; k <= shipCrdnt[2]; k++) {
                         if (!field[k][shipCrdnt[1]].equals("X")) {
                              flag = false;
                         }
                    }
               }
               if (flag) {
                    Arrays.fill(shipCrdnt, 0);
                    return true;
               }
          }
          return false;
     }

     public int takeShot(int[] crdnt, int count) {
          switch (this.field[crdnt[0]][crdnt[1]]) {
               case BUSY -> {
                    this.field[crdnt[0]][crdnt[1]] = "X";
                    if (isSankShip()) {
                         System.out.println(count);
                         if (count == BattleField.KindsOfShips.cellsToHit() - 1) {
                              System.out.println("You sank the last ship. You won. Congratulations!");
                         } else {
                              System.out.println("You sank a ship!");
                              pressEnter();
                         }
                    } else {
                         System.out.println("You hit a ship!");
                         pressEnter();
                    }
                    return 1;
               }
               case "X" -> {
                    this.output(true);
                    System.out.println("You hit a ship!");
                    pressEnter();
                    return 0;
               }
               case "~", "M" -> {
                    this.field[crdnt[0]][crdnt[1]] = "M";
                    System.out.println("You missed!");
                    pressEnter();
                    return 0;
               }
               default -> {
                    return 0;
               }
          }
     }
}
