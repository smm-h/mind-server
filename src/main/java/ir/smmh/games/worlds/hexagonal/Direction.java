package ir.smmh.games.worlds.hexagonal;

public enum Direction {
    LEFT, RIGHT, L_UP, R_UP, L_DOWN, R_DOWN;

    public static Direction makeHorizontal(boolean left) {
        if (left)
            return LEFT;
        else
            return RIGHT;
    }

    public static Direction makeVertical(boolean left, boolean up) {
        if (left && up)
            return L_UP;
        else if (left)
            return L_DOWN;
        else if (up)
            return R_UP;
        else
            return R_DOWN;
    }
}
