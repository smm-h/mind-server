package ir.smmh.util.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.Form;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SingleValueBlankSpace implements Form.BlankSpace {
    private final String title;
    private final @NotNull String prefix, suffix;
    private final @Nullable String ifLeftBlank;

    public SingleValueBlankSpace(String title) {
        this.title = title;
        this.prefix = "";
        this.suffix = "";
        this.ifLeftBlank = null;
    }

    public SingleValueBlankSpace(String title, String ifLeftBlank) {
        this.title = title;
        this.prefix = "";
        this.suffix = "";
        this.ifLeftBlank = ifLeftBlank;
    }

    public SingleValueBlankSpace(String title, String prefix, String suffix) {
        this.title = title;
        this.prefix = prefix;
        this.suffix = suffix;
        this.ifLeftBlank = null;
    }

    public SingleValueBlankSpace(String title, String prefix, String suffix, String ifLeftBlank) {
        this.title = title;
        this.prefix = prefix;
        this.suffix = suffix;
        this.ifLeftBlank = ifLeftBlank;
    }

    @Override
    public @NotNull String compose(@NotNull Sequential<String> values) throws Form.IncompleteFormException {
        if (values.isEmpty()) {
            if (ifLeftBlank == null) throw new Form.IncompleteFormException(this);
            return ifLeftBlank;
        } else {
            return prefix + values.getSingleton() + suffix;
        }
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    @Override
    public int getMinimumCount() {
        return ifLeftBlank == null ? 1 : 0;
    }

    @Override
    public int getMaximumCount() {
        return 1;
    }

}
