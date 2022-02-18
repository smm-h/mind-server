package ir.smmh.apps.plotbot;

import java.awt.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PlotByXBotStyles {
    private static PlotByXBotStyles instance;
    private final Map<String, Color> colors = new HashMap<>();
    private final Map<String, String> colorEmojis = new HashMap<>();
    private final Map<String, Stroke> strokes = new HashMap<>();

    private PlotByXBotStyles() {
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
        addStroke("thick", 7);
        addStroke("veryThick", 12);
    }

    public static PlotByXBotStyles getInstance() {
        return instance == null ? (instance = new PlotByXBotStyles()) : instance;
    }


    private void addColor(String name, String emoji, Color color) {
        colors.put(name, color);
        colorEmojis.put(name, emoji);
    }

    private void addStroke(String name, int width) {
        addStroke(name.toLowerCase(Locale.ROOT), new BasicStroke(width));
    }

    public boolean isColor(String name) {
        return colors.containsKey(name);
    }

    public boolean isStroke(String name) {
        return strokes.containsKey(name);
    }

    public String getColorEmoji(String color) {
        var emoji = colorEmojis.get(color);
        if (emoji == null)
            return "️•";
        return emoji;
    }

    private void addStroke(String name, Stroke stroke) {
        strokes.put(name.toLowerCase(Locale.ROOT), stroke);
    }

    public Stroke getStroke(String name) {
        return strokes.get(name);
    }

    public Color getColor(String name) {
        var namedColor = colors.get(name);
        if (namedColor == null)
            return new Color(Integer.parseInt(name));
        return namedColor;
    }
}
