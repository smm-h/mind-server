package ir.smmh.apps.plotbot;

import ir.smmh.tgbot.Bots;
import ir.smmh.tgbot.impl.SimpleBotImpl;
import ir.smmh.util.Map;
import ir.smmh.util.impl.MapImpl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PlotBot extends SimpleBotImpl {
    private final Map.SingleValue.Mutable<String, Function> functions = new MapImpl.SingleValue.Mutable<>();

    public static void main(String[] args) {
        new PlotBot().start(Bots.r5bot);
    }

    @Override
    public void process(long chatId, String text, int messageId) {
        Function f = x -> x * x;
        File file = new File(text.hashCode() + ".png");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    sendMessage(chatId, "Unexpected error regarding the existence of image file", messageId);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                sendMessage(chatId, "I/O error while creating empty image file", messageId);
                return;
            }
            int w = 640;
            int h = 640;
            int hScale = 64;
            int vScale = 64;
            int hTicks = 1;
            int vTicks = 1;
            BufferedImage image = new BufferedImage(w * 2, h * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setColor((Color.GRAY));
            for (int x = 0; x <= w; x += hScale) {
                g.drawLine(w + x, 0, w + x, h * 2);
                g.drawLine(w - x, 0, w - x, h * 2);
            }
            for (int y = 0; y <= h; y += vScale) {
                g.drawLine(0, h + y, w * 2, h + y);
                g.drawLine(0, h - y, w * 2, h - y);
            }
            double x, y, x_prev = 0, y_prev = 0;
            for (x = -w; x <= w; x++) {
                y = -f.apply(x / hScale) * vScale;
                if (x > -w)
                    g.drawLine((int) x + w, (int) y + h, (int) x_prev + w, (int) y_prev + h);
                x_prev = x;
                y_prev = y;
            }
            try {
                ImageIO.write(image, "png", file);
            } catch (IOException e) {
                sendMessage(chatId, "I/O error while writing image file", messageId);
                return;
            }
        }
        try {
            sendPhoto(chatId, file, text, messageId);
        } catch (FileNotFoundException e) {
            sendMessage(chatId, "Image file not found", messageId);
        }
    }

    private interface Function {
        double apply(double x);
    }
}
