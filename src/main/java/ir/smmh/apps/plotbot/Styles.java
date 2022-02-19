package ir.smmh.apps.plotbot;

import ir.smmh.apps.plotbot.impl.StylesImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public interface Styles {
    static Styles getInstance() {
        return StylesImpl.getInstance();
    }

    void addColor(String name, String emoji, Color color);

    void addStroke(String name, int width);

    boolean isColor(String name);

    boolean isStroke(String name);

    @Nullable String getColorEmoji(String color);

    void addStroke(String name, Stroke stroke);

    @Nullable Stroke getStroke(String name);

    @Nullable Color getColor(String name);

    @NotNull Iterable<String> getStrokeNames();

    @NotNull Iterable<String> getColorNames();
}
