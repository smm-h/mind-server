package ir.smmh.games.worlds.hexagonal.impl;

import ir.smmh.games.worlds.hexagonal.Cell;
import ir.smmh.games.worlds.hexagonal.World;

public class CellImpl implements Cell {

    private final int index;
    private final World<?> world;
    private final int column, row;

    public CellImpl(World<?> world, int index, int column, int row) {
        this.world = world;
        this.index = index;
        this.column = column;
        this.row = row;
    }

    @Override
    public World<?> getWorld() {
        return world;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return "(" + column + ", " + row + ")";
    }
}
