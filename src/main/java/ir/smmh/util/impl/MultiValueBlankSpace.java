package ir.smmh.util.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.Form;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public class MultiValueBlankSpace extends Form.BlankSpace.ZeroOrMore {
    private final @NotNull String beforeEach, betweenEach, afterEach, beforeAll, afterAll, ifLeftBlank;

    public MultiValueBlankSpace(String title, String betweenEach, String ifLeftBlank) {
        super(title);
        this.beforeEach = "";
        this.betweenEach = betweenEach;
        this.afterEach = "";
        this.beforeAll = "";
        this.afterAll = "";
        this.ifLeftBlank = ifLeftBlank;
    }

    public MultiValueBlankSpace(String title, String beforeEach, String afterEach, String ifLeftBlank) {
        super(title);
        this.beforeEach = beforeEach;
        this.betweenEach = "";
        this.afterEach = afterEach;
        this.beforeAll = "";
        this.afterAll = "";
        this.ifLeftBlank = ifLeftBlank;
    }

    public MultiValueBlankSpace(String title, String beforeEach, String betweenEach, String afterEach, String ifLeftBlank) {
        super(title);
        this.beforeEach = beforeEach;
        this.betweenEach = betweenEach;
        this.afterEach = afterEach;
        this.beforeAll = "";
        this.afterAll = "";
        this.ifLeftBlank = ifLeftBlank;
    }

    public MultiValueBlankSpace(String title, String beforeEach, String afterEach, String beforeAll, String afterAll, String ifLeftBlank) {
        super(title);
        this.beforeEach = beforeEach;
        this.betweenEach = "";
        this.afterEach = afterEach;
        this.beforeAll = beforeAll;
        this.afterAll = afterAll;
        this.ifLeftBlank = ifLeftBlank;
    }

    public MultiValueBlankSpace(String title, String beforeEach, String betweenEach, String afterEach, String beforeAll, String afterAll, String ifLeftBlank) {
        super(title);
        this.beforeEach = beforeEach;
        this.betweenEach = betweenEach;
        this.afterEach = afterEach;
        this.beforeAll = beforeAll;
        this.afterAll = afterAll;
        this.ifLeftBlank = ifLeftBlank;
    }

    @Override
    public @NotNull String compose(@NotNull Sequential<String> values) {
        if (values.isEmpty()) {
            return ifLeftBlank;
        } else {
            StringJoiner j = new StringJoiner(betweenEach, beforeAll, afterAll);
            for (String value : values) j.add(beforeEach + value + afterEach);
            return j.toString();
        }
    }
}
