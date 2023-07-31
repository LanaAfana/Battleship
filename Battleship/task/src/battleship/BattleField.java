package battleship;


public class BattleField {
     private String[][] field;
     private int size;
     private static final String BUSY = "O";

     BattleField(int size) {
          this.size = size;
          field = new String[size][size];
          for (int i = 0; i < size; i++)
               for (int j = 0; j < size; j++) {
                    field[i][j] = "~";
               }
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

     private boolean isNumber(int number) {
          return (number >= 0 && number < this.size);
     }

     private boolean isCorrectLength(int step, int[] crdnts) {
          int length = (crdnts[0] == crdnts[2]) ? Math.abs(crdnts[1] - crdnts[3]) + 1:
                  Math.abs(crdnts[0] - crdnts[2]) + 1;
          if (length != KindsOfShips.values()[step].numOfCells) {
               return false;
          }
          return true;
     }

     private boolean isCorrectLocation(int[] crdnts) {
          if ((crdnts[0] == crdnts[2] && crdnts[1] == crdnts[3]) ||
                  (crdnts[1] != crdnts[3] && crdnts[0] != crdnts[2])) {
               return false;
          }
          return true;
     }

     private boolean isCloseTo(int[] crdnts) {
          int i1 = (crdnts[0] == 0) ? 0 : crdnts[0] - 1;
          int i2 = (crdnts[2] == field.length - 1) ? crdnts[2] : crdnts[2] + 1;
          int j1 = (crdnts[1] == 0) ? 0 : crdnts[1] - 1;
          int j2 = (crdnts[3] == field.length - 1) ? crdnts[3] : crdnts[3] + 1;

          if (crdnts[0] == crdnts[2]) {
               for (int j = j1; j <= j2; j++) {
                    if (field[crdnts[0]][j] == BUSY) return true;
               }
               for (int i = i1; i <= i2; i++)
                    for (int j = crdnts[1]; j <= crdnts[3]; j++) {
                         if (field[i][j] == BUSY) return true;
                    }
          } else {
               for (int i= i1; i <= i2; i++) {
                    if (field[i][crdnts[1]] == BUSY) return true;
               }
               for (int i = crdnts[0]; i <= crdnts[2]; i++)
                    for (int j = j1; j <= j2; j++) {
                         if (field[i][j] == BUSY) return true;
                    }
          }
          return false;
     }

     int[] getCoordinates(String input, int step) {
          int[] result = new int[4];
          String x = getRawX(input);
          String y = getRawY(input);
          String numPart = "";
          try {
               numPart = (x.length() == 2) ? String.valueOf(x.charAt(1)) :
                       String.valueOf(x.charAt(1)) + String.valueOf(x.charAt(2));
               int c1 = Integer.parseInt(numPart) - 1;
               numPart = (y.length() == 2) ? String.valueOf(y.charAt(1)) :
                       String.valueOf(y.charAt(1)) + String.valueOf(y.charAt(2));
               int c2 = Integer.parseInt(numPart) - 1;
               result[0] = Math.min(c1, c2);
               result[2] = Math.max(c1, c2);
               c1 = letterToDigit(x.charAt(0));
               c2 = letterToDigit(y.charAt(0));
               result[1] = Math.min(c1, c2);
               result[3] = Math.max(c1, c2);
          } catch (NumberFormatException nfe) {
               System.out.println("Error! Invalid input. Try again:");
          }
          return isCoordinates(result, step) ? result : new int[1];
     }

     public boolean isCoordinates(int[] crdnts, int step) {
          if (!isNumber(crdnts[1]) || !isNumber(crdnts[0]) || !isNumber(crdnts[3]) || !isNumber(crdnts[2])) {
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

     public void output() {
          StringBuilder output = new StringBuilder("  ");

          for (int i = 1; i <= size; i++) {
               output.append(i + " ");
          }
          output.append("\n");
          for (int i = 0; i < size; i++) {
               for (int j = 0; j < size; j++) {
                    if (j == 0) {
                         output.append(digitToLetter(i) + " ");
                    }
                    output.append(field[j][i] + " ");
               }
               output.append("\n");
          }
          System.out.println(output);
     }

     public void addShip(int[] crdnts) {
          for (int i = crdnts[0]; i <= crdnts[2]; i++){
               for (int j = crdnts[1]; j <= crdnts[3]; j++) {
                    this.field[i][j] = BUSY;
               }
          }
     }
}
