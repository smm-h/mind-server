package ir.smmh.util.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.Map;
import ir.smmh.util.Mutable;
import ir.smmh.util.Form;
import ir.smmh.util.jile.Or;
import ir.smmh.util.jile.impl.FatOr;
import org.jetbrains.annotations.NotNull;

public class FormImpl implements Form, Mutable.WithListeners.Injected {
    private final Sequential.Mutable.VariableSize<Or<String, BlankSpace>> sequence;
    private transient String string = null;
    private transient int size = -1;
    private transient boolean isFilledOut = false;

    public FormImpl() {
        this(new SequentialImpl<>());
    }

    private FormImpl(Sequential.Mutable.VariableSize<Or<String, BlankSpace>> sequence) {
        this.sequence = sequence;
        getOnCleanListeners().add(() -> {
            isFilledOut = false;
            for (var i : sequence) {
                if (i.isThat()) {
                    return;
                }
            }
            isFilledOut = true;
            size = 0;
            for (var i : sequence) {
                size += i.getThis().length();
            }
            StringBuilder b = new StringBuilder(size);
            for (var i : sequence) {
                b.append(i.getThis());
            }
            string = b.toString();
        });
    }

    private static Or<String, BlankSpace> make(String string) {
        return FatOr.makeThis(string);
    }

    private static Or<String, BlankSpace> make(BlankSpace blankSpace) {
        return FatOr.makeThat(blankSpace);
    }

    @Override
    public boolean isFilledOut() {
        return clean() && isFilledOut;
    }

    @Override
    public Form clone(boolean deepIfPossible) {
        return new FormImpl(sequence.clone(false));
    }

    @Override
    public Form specificThis() {
        return this;
    }

    @Override
    public @NotNull Form append(BlankSpace blankSpace) {
        sequence.append(make(blankSpace));
        return this;
    }

    @Override
    public @NotNull Form prepend(BlankSpace blankSpace) {
        sequence.prepend(make(blankSpace));
        return this;
    }

    @Override
    public @NotNull Form append(Form form) {
        sequence.appendAll(form.getSequence());
        return this;
    }

    @Override
    public @NotNull Form prepend(Form form) {
        sequence.prependAll(form.getSequence());
        return this;
    }

    @Override
    public @NotNull Form append(String text) {
        sequence.append(make(text));
        return this;
    }

    @Override
    public @NotNull Form prepend(String text) {
        sequence.prepend(make(text));
        return this;
    }

    @Override
    public @NotNull Form append(char c) {
        sequence.append(make(Character.toString(c)));
        return this;
    }

    @Override
    public @NotNull Form prepend(char c) {
        sequence.prepend(make(Character.toString(c)));
        return this;
    }

    @Override
    public @NotNull Form leaveBlank(BlankSpace blankSpace) {
        fillRaw(blankSpace, blankSpace.leaveBlank());
        return this;
    }

    @Override
    public @NotNull Form fillOut(BlankSpace.SingleValue blankSpace, String value) {
        fillRaw(blankSpace, blankSpace.enterValue(value));
        return this;
    }

    @Override
    public @NotNull Form fillOut(BlankSpace.MultiValue blankSpace, Iterable<String> values) {
        fillRaw(blankSpace, blankSpace.enterValues(values));
        return this;
    }

    private void fillRaw(BlankSpace blankSpace, String rawValue) {
        var k = make(blankSpace);
        var v = make(rawValue);
        int n = sequence.getSize();
        for (int i = 0; i < n; i++) {
            if (sequence.getAtIndex(i).equalTo(k)) {
                sequence.setAtIndex(i, v);
            }
        }
    }

    @Override
    public @NotNull Form fillOut(Map.MultiValue<BlankSpace, String> map) {
        for (BlankSpace k : map.overKeys()) {
            Iterable<String> v = map.getAtPlace(k);
            int n = FunctionalUtil.capacityNeededForIterateIfNull(v);
            if (k instanceof BlankSpace.SingleValue) {
                if (n == 0) {
                    leaveBlank(k);
                } else if (n == 1) {
                    fillOut((BlankSpace.SingleValue) k, v.iterator().next());
                } else {
                    throw new UnsupportedOperationException("cannot fill out single value blank space with iterable");
                }
            } else if (k instanceof BlankSpace.MultiValue) {
                fillOut((BlankSpace.MultiValue) k, v);
            } else {
                throw new UnsupportedOperationException("blank space must be either single-value or multi-value");
            }
        }
        return this;
    }

    @Override
    public @NotNull Sequential<Or<String, BlankSpace>> getSequence() {
        return sequence;
    }

    @Override
    public String toString() {
        return clean() ? string : null;
    }

    @Override
    public @NotNull Mutable.WithListeners getInjectedMutable() {
        return sequence;
    }
}
