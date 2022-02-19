package ir.smmh.apps.plotbot.impl;

import ir.smmh.apps.plotbot.Styles;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StylesImpl implements Styles {
    private static Styles instance;
    private final Map<String, Color> colors = new HashMap<>();
    private final Map<String, String> colorEmojis = new HashMap<>();
    private final Map<String, Stroke> strokes = new HashMap<>();

    private StylesImpl() {
        addColor("brown", "&#128996;", new Color(102, 51, 0));
        addColor("red", "&#128308;", new Color(255, 51, 51));
        addColor("orange", "&#128992;", new Color(255, 102, 0));
        addColor("yellow", "&#128993;", new Color(255, 204, 0));
        addColor("green", "&#128994;", new Color(0, 153, 0));
        addColor("blue", "&#128309;", new Color(51, 153, 255));
        addColor("purple", "&#128995;", new Color(102, 0, 153));
        addColor("black", "&#9899;&#65039;", new Color(51, 51, 51));
        addColor("white", "&#9898;&#65039;", new Color(204, 204, 204));
        addStroke("veryThin", 1);
        addStroke("thin", 3);
        addStroke("thick", 5);
        addStroke("veryThick", 7);
    }

    public static Styles getInstance() {
        return StylesImpl.instance == null ? (StylesImpl.instance = new StylesImpl()) : StylesImpl.instance;
    }

    @Override
    public void addColor(String name, String emoji, Color color) {
        colors.put(name, color);
        colorEmojis.put(name, emoji);
    }

    @Override
    public void addStroke(String name, int width) {
        addStroke(name.toLowerCase(Locale.ROOT), new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    @Override
    public void addStroke(String name, Stroke stroke) {
        strokes.put(name.toLowerCase(Locale.ROOT), stroke);
    }

    @Override
    public boolean isColor(String name) {
        return colors.containsKey(name);
    }

    @Override
    public boolean isStroke(String name) {
        return strokes.containsKey(name);
    }

    @Override
    public String getColorEmoji(String color) {
        return colorEmojis.get(color);
    }

    @Override
    public Stroke getStroke(String name) {
        return strokes.get(name);
    }

    @Override
    public Color getColor(String name) {
        return colors.get(name);
    }

    @Override
    public @NotNull Iterable<String> getStrokeNames() {
        return strokes.keySet();
    }

    @Override
    public @NotNull Iterable<String> getColorNames() {
        return colors.keySet();
    }
}
