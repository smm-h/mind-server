package ir.smmh.util;

import java.awt.*;

public interface GraphicsUtil {
    static Color changeTransparency(Color color, float transparency) {
        return new Color((color.getRGB() & 0x00ffffff) | getAlpha(transparency), true);
    }

    static int getAlpha(float transparency) {
        return ((int) (transparency * 255f)) << 24;
    }
}
