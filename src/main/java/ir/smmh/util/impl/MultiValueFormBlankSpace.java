package ir.smmh.util.impl;

import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.Form;
import org.jetbrains.annotations.NotNull;

public class MultiValueFormBlankSpace implements Form.BlankSpace.MultiValue {
    private final boolean canBeLeftBlank;
    private final @NotNull String beforeEach, afterEach, beforeAll, afterAll, ifLeftBlank;

    public MultiValueFormBlankSpace(String beforeEach, String afterEach, String beforeAll, String afterAll) {
        this.canBeLeftBlank = false;
        this.beforeEach = beforeEach;
        this.afterEach = afterEach;
        this.beforeAll = beforeAll;
        this.afterAll = afterAll;
        this.ifLeftBlank = "";
    }

    public MultiValueFormBlankSpace(String beforeEach, String afterEach, String beforeAll, String afterAll, String ifLeftBlank) {
        this.canBeLeftBlank = true;
        this.beforeEach = beforeEach;
        this.afterEach = afterEach;
        this.beforeAll = beforeAll;
        this.afterAll = afterAll;
        this.ifLeftBlank = ifLeftBlank;
    }

    @Override
    public boolean canBeLeftBlank() {
        return canBeLeftBlank;
    }

    @Override
    public @NotNull String enterValues(Iterable<String> values) {
        if (FunctionalUtil.isEmpty(values)) {
            if (canBeLeftBlank) return ifLeftBlank;
            else throw new NullPointerException("cannot leave this space blank");
        } else {
            StringBuilder b = new StringBuilder();
            b.append(beforeAll);
            for (String value : values) {
                b.append(beforeEach).append(value).append(afterEach);
            }
            b.append(afterAll);
            return b.toString();
        }
    }
}
