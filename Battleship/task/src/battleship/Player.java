package battleship;

public class Player {
    private final BattleField field;
    private int count;

    Player(BattleField field) {
        this.field = field;
        this.count = 0;
    }

    public BattleField getField() {
        return field;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public int getCount() {
        return this.count;
    }
}
