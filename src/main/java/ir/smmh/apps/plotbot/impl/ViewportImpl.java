package ir.smmh.apps.plotbot.impl;

import ir.smmh.apps.plotbot.Viewport;
import ir.smmh.lingu.Maker;

public class ViewportImpl implements Viewport {
    public static final Viewport DEFAULT = new ViewportImpl(8);
    public static final double AREA = 1048576;
    private static final int MAX_RATIO = 3;
    private final double x, y, hRadius, vRadius, hScale, vScale;

    private ViewportImpl(double radius) {
        this(0, 0, radius);
    }

    private ViewportImpl(double centerX, double centerY, double radius) {
        this(centerX, centerY, radius, radius);
    }

    private ViewportImpl(double centerX, double centerY, double horizontalRadius, double verticalRadius) {
        x = centerX;
        y = centerY;
        hRadius = horizontalRadius;
        vRadius = verticalRadius;
        double ratio = hRadius / vRadius;
        double reach = Math.sqrt(AREA / ratio);
        hScale = reach / hRadius * ratio;
        vScale = reach / vRadius;
    }

    public static Viewport make(double centerX, double centerY, double horizontalRadius, double verticalRadius) throws Maker.MakingException {
        if (horizontalRadius <= 0)
            throw new Maker.MakingException("Horizontal radius must be higher than zero");
        if (verticalRadius <= 0)
            throw new Maker.MakingException("Vertical radius must be higher than zero");
        if (Math.max(horizontalRadius, verticalRadius) / Math.min(horizontalRadius, verticalRadius) > MAX_RATIO)
            throw new Maker.MakingException("The ratio of radii cannot be more than 1:" + MAX_RATIO);
        return new ViewportImpl(centerX, centerY, horizontalRadius, verticalRadius);
    }

    @Override
    public double getHorizontalScale() {
        return hScale;
    }

    @Override
    public double getVerticalScale() {
        return vScale;
    }

    @Override
    public double getCenterX() {
        return x;
    }

    @Override
    public double getCenterY() {
        return y;
    }

    @Override
    public double getHorizontalRadius() {
        return hRadius;
    }

    @Override
    public double getVerticalRadius() {
        return vRadius;
    }

    @Override
    public String toString() {
        return String.format("[%.2f, %.2f, %.2f, %.2f]"
                , x - hRadius
                , y - vRadius
                , x + hRadius
                , y + vRadius
        );
    }
}
