package ir.smmh.apps.plotbot;

public interface Viewport {

    double getCenterX();

    double getCenterY();

    double getHorizontalRadius();

    double getVerticalRadius();

    double getHorizontalScale();

    double getVerticalScale();

    default int getPixelX(double realX) {
        return (int) ((realX - getCenterX() + getHorizontalRadius()) * getHorizontalScale());
    }

    default int getPixelY(double realY) {
        return getHeight() - (int) ((realY - getCenterY() + getVerticalRadius()) * getVerticalScale());
    }

    default double getRealX(int pixelX) {
        return pixelX / getHorizontalScale() + getCenterX() - getHorizontalRadius();
    }

    default double getRealY(int pixelY) {
        return (-pixelY - getHeight()) / getVerticalScale() + getCenterY() - getVerticalRadius();
    }

    default int getWidth() {
        return (int) (getHorizontalScale() * getHorizontalRadius() * 2);
    }

    default int getHeight() {
        return (int) (getVerticalScale() * getVerticalRadius() * 2);
    }
}
