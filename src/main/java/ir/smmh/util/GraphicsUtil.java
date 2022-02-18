package ir.smmh.util;

import java.awt.*;

public interface GraphicsUtil {
    static Color changeTransparency(Color color, float transparency) {
        return new Color((color.getRGB() & 0x00ffffff) | getAlpha(transparency), true);
    }

    static int getAlpha(float transparency) {
        return ((int) (transparency * 255f)) << 24;
    }


    static Color multiply(Color color, int value) {
        double factor = value / 255.;
        return new Color(
                (int) (color.getRed() * factor),
                (int) (color.getGreen() * factor),
                (int) (color.getBlue() * factor),
                color.getAlpha());
    }

}
