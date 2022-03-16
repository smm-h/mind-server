package ir.smmh.games.worlds.hexagonal;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanGetAtIndex;

import java.util.Set;

public interface World<C extends Cell> extends CanGetAtIndex<C>, Iterable<C> {

    double HEIGHT_FACTOR = Math.sqrt(3) / 3.0;

    static double getHeight(double scale) {
        return scale * HEIGHT_FACTOR;
    }

    static double getSideLength(double scale) {
        return getHeight(scale) * HEIGHT_FACTOR * 2;
    }

    static double getPixelX(int side, int column, int row, int x0, double scale) {
        return x0 + (Math.abs(side - 1 - row) + column * 2) * World.getHeight(scale);
    }

    static double getPixelY(int side, int column, int row, int y0, double scale) {
        return y0 + row * scale;
    }

    int getSide();

    int getCellIndex(int column, int row);

    Set<Integer> getCellCircle(int center, int radius);

    Set<Integer> getCellLayer(int center, int radius);

    Sequential<Integer> getCorners();

    int move(int index, Direction direction);

    int distance(int a, int b);

}
