package ir.smmh.tgbot;

import ir.smmh.util.MarkupFragment;
import ir.smmh.util.MarkupWriter;
import ir.smmh.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public abstract class TelegramMarkupList implements MarkupWriter.List {

    private final StringBuilder builder = new StringBuilder();

    @Override
    public @NotNull MarkupFragment build() {
        return new MarkupFragment(builder.toString());
    }

    @Override
    public @NotNull MarkupWriter.List append(MarkupFragment item) {
        builder.append(getBullet()).append(item.getData()).append("\n");
        return this;
    }

    protected abstract String getBullet();

    @Override
    public @NotNull MarkupWriter.List nest(MarkupWriter.List innerList) {
        builder.append(StringUtil.shiftRight(innerList.build().getData(), 2));
        return this;
    }

    public static class Dash extends TelegramMarkupList {
        @Override
        protected String getBullet() {
            return "- ";
        }
    }

    public static class Numbered extends TelegramMarkupList {
        private int n = 0;

        @Override
        protected String getBullet() {
            return ++n + ". ";
        }
    }
}
