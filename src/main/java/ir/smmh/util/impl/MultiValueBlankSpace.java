package ir.smmh.util.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.Form;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public class MultiValueBlankSpace implements Form.BlankSpace.ZeroOrMore {
    private final @NotNull String beforeEach, betweenEach, afterEach, beforeAll, afterAll, ifLeftBlank;

    public MultiValueBlankSpace(String beforeEach, String betweenEach, String afterEach, String ifLeftBlank) {
        this.beforeEach = beforeEach;
        this.betweenEach = betweenEach;
        this.afterEach = afterEach;
        this.beforeAll = "";
        this.afterAll = "";
        this.ifLeftBlank = ifLeftBlank;
    }
    public MultiValueBlankSpace(String beforeEach, String betweenEach, String afterEach, String beforeAll, String afterAll, String ifLeftBlank) {
        this.beforeEach = beforeEach;
        this.betweenEach = betweenEach;
        this.afterEach = afterEach;
        this.beforeAll = beforeAll;
        this.afterAll = afterAll;
        this.ifLeftBlank = ifLeftBlank;
    }

    @Override
    public @NotNull String enterValues(Sequential<String> values) {
        if (values.isEmpty()) {
            return ifLeftBlank;
        } else {
            StringJoiner j = new StringJoiner(betweenEach, beforeAll, afterAll);
            for (String value : values) j.add(beforeEach + value + afterEach);
            return j.toString();
        }
    }
}
