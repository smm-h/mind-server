package ir.smmh.util.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.Form;
import org.jetbrains.annotations.NotNull;

public class SingleValueBlankSpace implements Form.BlankSpace {
    private final boolean canBeLeftBlank;
    private final @NotNull String prefix, suffix, ifLeftBlank;

    public SingleValueBlankSpace() {
        this.canBeLeftBlank = false;
        this.prefix = "";
        this.suffix = "";
        this.ifLeftBlank = "";
    }

    public SingleValueBlankSpace(String ifLeftBlank) {
        this.canBeLeftBlank = true;
        this.prefix = "";
        this.suffix = "";
        this.ifLeftBlank = ifLeftBlank;
    }

    public SingleValueBlankSpace(String prefix, String suffix) {
        this.canBeLeftBlank = false;
        this.prefix = prefix;
        this.suffix = suffix;
        this.ifLeftBlank = "";
    }

    public SingleValueBlankSpace(String prefix, String suffix, String ifLeftBlank) {
        this.canBeLeftBlank = true;
        this.prefix = prefix;
        this.suffix = suffix;
        this.ifLeftBlank = ifLeftBlank;
    }

    @Override
    public @NotNull String enterValues(Sequential<String> values) {
        if (values.isEmpty()) {
            if (canBeLeftBlank) return ifLeftBlank;
            else throw new NullPointerException("cannot leave this space blank");
        } else {
            return prefix + values.getSingleton() + suffix;
        }
    }

    @Override
    public boolean isInexhaustible() {return false;}

    @Override
    public boolean acceptsLength(int length) {
        return length == 1 || (length == 0 && canBeLeftBlank);
    }
}
