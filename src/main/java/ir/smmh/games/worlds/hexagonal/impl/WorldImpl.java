package ir.smmh.games.worlds.hexagonal.impl;

import ir.smmh.games.worlds.hexagonal.Cell;
import ir.smmh.games.worlds.hexagonal.Direction;
import ir.smmh.games.worlds.hexagonal.World;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class WorldImpl<C extends Cell> implements World<C> {
    private final int side;
    private final C[] cells;
    private final @NotNull Sequential<Integer> corners;

    @SuppressWarnings("unchecked")
    public WorldImpl(int side, CellConstructor<C> constructor) {
        this.side = side;
        int n = 3 * (side * (side - 1) + 1) - 2;
        cells = (C[]) new Cell[n];
        for (int i = 0; i < n; i++) {
            int r = 0;
            int c = i;
            while (c >= getColumnsInRow(r)) {
                c -= getColumnsInRow(r);
                r++;
            }
            cells[i] = constructor.construct(this, i, c, r);
        }
        int right = side * (3 * side - 1) / 2 - 1;
        corners = Sequential.ofArguments(right, side - 1, 0, right - side * 2 + 2, n - side, n - 1);
    }

    @Override
    public int getSide() {
        return side;
    }

    @Override
    public int getCellIndex(int column, int row) {
        if (column < 0 || row < 0 || column >= side * 2 || row >= side * 2) return -1;
        if (column >= getColumnsInRow(row)) return -1;
        int index = column;
        while (true) {
            row--;
            if (row < 0) break;
            index += getColumnsInRow(row);
        }
        return index < cells.length ? index : -1;
    }

    private int getColumnsInRow(int row) {
        return side * 2 - 1 - Math.abs(side - 1 - row);
    }

    @Override
    public @NotNull Set<Integer> getCellCircle(int center, int radius) {
        Set<Integer> set = Set.of(center);
        for (int i = 0; i < radius; i++) {
            set = spreadInEveryDirection(set);
        }
        if (radius == 1) set.add(center);
        return set;
    }

    private @NotNull Set<Integer> spreadInEveryDirection(Set<Integer> cells) {
        Set<Integer> set = new HashSet<>();
        for (int cell : cells) {
            for (Direction d : Direction.values()) {
                int t = move(cell, d);
                if (t != -1) set.add(t);
            }
        }
        return set;
    }

    @Override
    public @NotNull Set<Integer> getCellLayer(int center, int radius) {
        Set<Integer> inner = Set.of(center);
        for (int i = 0; i < radius - 1; i++) {
            inner = spreadInEveryDirection(inner);
        }
        Set<Integer> outer = spreadInEveryDirection(inner);
        outer.removeAll(inner);
        if (radius == 2) outer.remove(center);
        return outer;
    }

    @Override
    public @NotNull Sequential<Integer> getCorners() {
        return corners;
    }

    /**
     * @param row Row index
     * @return -1 if row is in top half, +1 if row is in bottom half, and 0 if
     * row is the middle row.
     */
    private int getRowSituation(int row) {
        return Integer.compare(row, side - 1);
    }

    @Override
    public int move(int index, Direction direction) {
        C cell = getAtIndex(index);
        int c = cell.getColumn();
        int r = cell.getRow();
        int s = getRowSituation(r);
        switch (direction) {
            case LEFT:
                return getCellIndex(c - 1, r);
            case RIGHT:
                return getCellIndex(c + 1, r);
            case L_UP:
                return getCellIndex(s > 0 ? c : c - 1, r - 1);
            case R_UP:
                return getCellIndex(s > 0 ? c + 1 : c, r - 1);
            case L_DOWN:
                return getCellIndex(s < 0 ? c : c - 1, r + 1);
            case R_DOWN:
                return getCellIndex(s < 0 ? c + 1 : c, r + 1);
        }
        return -1;
    }

    private int compromiseRowBetweenRows(int r1, int r2) {
        double m = (r1 + r2) / 2.0;
        return (int) (m > side - 1 ? Math.floor(m) : Math.ceil(m));
    }

    @Override
    public int distance(int p1, int p2) {
        if (p1 == p2) return 0;
        int c1 = cells[p1].getColumn();
        int r1 = cells[p1].getRow();
        int c2 = cells[p2].getColumn();
        int r2 = cells[p2].getRow();
        if (r1 == r2) return Math.abs(c1 - c2);
        int x1 = Math.abs(side - 1 - r1) + c1 * 2;
        int x2 = Math.abs(side - 1 - r2) + c2 * 2;
        int verticalMoves = 0;
        while (r1 != r2) {
            p1 = move(p1, Direction.makeVertical(x1 > x2, r1 > r2));
            c1 = cells[p1].getColumn();
            r1 = cells[p1].getRow();
            x1 = Math.abs(side - 1 - r1) + c1 * 2;
            verticalMoves++;
        }
        return Math.abs(c1 - c2) + verticalMoves;
    }

    @NotNull
    @Override
    public Iterator<C> iterator() {
        return ArrayUtil.makeIterator(cells);
    }

    @Override
    public int getSize() {
        return cells.length;
    }

    @Override
    public C getAtIndex(int index) {
        return cells[index];
    }

    @FunctionalInterface
    public interface CellConstructor<C extends Cell> {
        C construct(World<?> world, int index, int column, int row);
    }

}