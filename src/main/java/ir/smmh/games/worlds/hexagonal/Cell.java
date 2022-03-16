package ir.smmh.games.worlds.hexagonal;

public interface Cell {

    World<?> getWorld();

    int getIndex();

    int getColumn();

    int getRow();
}
