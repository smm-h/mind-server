package ir.smmh.apps.plotbot.impl;

import ir.smmh.apps.plotbot.Viewport;
import ir.smmh.lingu.Maker;

public class ViewportImpl implements Viewport {
    public static final Viewport DEFAULT = new ViewportImpl(0, 0, 8, 8);
    private static final int MAX_RATIO = 3;
    private final double originX, originY, horizontalRadius, verticalRadius;

    private ViewportImpl(double originX, double originY, double horizontalRadius, double verticalRadius) {
        this.originX = originX;
        this.originY = originY;
        this.horizontalRadius = horizontalRadius;
        this.verticalRadius = verticalRadius;
    }

    public static Viewport make(double originX, double originY, double horizontalRadius, double verticalRadius) throws Maker.MakingException {
        if (horizontalRadius <= 0)
            throw new Maker.MakingException("Horizontal radius must be higher than zero");
        if (verticalRadius <= 0)
            throw new Maker.MakingException("Vertical radius must be higher than zero");
        if (Math.max(horizontalRadius, verticalRadius) / Math.min(horizontalRadius, verticalRadius) > MAX_RATIO)
            throw new Maker.MakingException("The ratio of radii cannot be more than 1:" + MAX_RATIO);
        return new ViewportImpl(originX, originY, horizontalRadius, verticalRadius);
    }

    @Override
    public double getOriginX() {
        return originX;
    }

    @Override
    public double getOriginY() {
        return originY;
    }

    @Override
    public double getHorizontalRadius() {
        return horizontalRadius;
    }

    @Override
    public double getVerticalRadius() {
        return verticalRadius;
    }

    @Override
    public String toString() {
        return String.format("[%.2f, %.2f, %.2f, %.2f]"
                , originX - horizontalRadius
                , originY - verticalRadius
                , originX + horizontalRadius
                , originY + verticalRadius
        );
    }
}
