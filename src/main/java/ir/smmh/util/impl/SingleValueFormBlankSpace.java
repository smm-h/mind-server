package ir.smmh.util.impl;

import ir.smmh.util.Form;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SingleValueFormBlankSpace implements Form.BlankSpace.SingleValue {
    private final boolean canBeLeftBlank;
    private final @NotNull String prefix, suffix, ifLeftBlank;

    public SingleValueFormBlankSpace(String prefix, String suffix) {
        this.canBeLeftBlank = false;
        this.prefix = prefix;
        this.suffix = suffix;
        this.ifLeftBlank = "";
    }

    public SingleValueFormBlankSpace(String prefix, String suffix, String ifLeftBlank) {
        this.canBeLeftBlank = true;
        this.prefix = prefix;
        this.suffix = suffix;
        this.ifLeftBlank = ifLeftBlank;
    }

    @Override
    public boolean canBeLeftBlank() {
        return canBeLeftBlank;
    }

    @Override
    public @NotNull String enterValue(@Nullable String value) {
        if (value == null) {
            if (canBeLeftBlank) return ifLeftBlank;
            else throw new NullPointerException("cannot leave this space blank");
        } else {
            return prefix + value + suffix;
        }
    }
}
