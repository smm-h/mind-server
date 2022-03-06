package ir.smmh.util.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.util.Form;
import ir.smmh.util.Map;
import ir.smmh.util.Mutable;
import ir.smmh.util.jile.Or;
import ir.smmh.util.jile.impl.FatOr;
import org.jetbrains.annotations.NotNull;

public class FormImpl implements Form, Mutable.WithListeners.Injected {
    private final Sequential.Mutable.VariableSize<Or<String, BlankSpace>> sequence;
    private final Map.MultiValue.Mutable<BlankSpace, String> map;
    private transient String string = null;
    private transient int size = -1;
    private transient boolean isFilledOut = false;

    public FormImpl() {
        this(new SequentialImpl<>(), new MapImpl.MultiValue.Mutable<>());
    }

    private FormImpl(Sequential.Mutable.VariableSize<Or<String, BlankSpace>> sequence, Map.MultiValue.Mutable<BlankSpace, String> map) {
        this.sequence = sequence;
        this.map = map;
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
    public Form clone(boolean deepIfPossible) {
        return new FormImpl(sequence.clone(false), map.clone(false));
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
    public @NotNull Form fillOut(BlankSpace blankSpace, Sequential<String> values) {
        map.addAllAtPlace(blankSpace, values);
        return this;
    }

    @Override
    public @NotNull Form fillOut(Map.MultiValue<BlankSpace, String> map) {
        for (BlankSpace k : map.overKeys())
            fillOut(k, map.getAtPlace(k));
        return this;
    }

    @Override
    public @NotNull Sequential<Or<String, BlankSpace>> getSequence() {
        return sequence;
    }

    @Override
    public boolean isFilledOut() {
        return clean() && isFilledOut;
    }

    @Override
    public String toString() {
        return isFilledOut() ? string : null;
    }

    @Override
    public @NotNull Mutable.WithListeners getInjectedMutable() {
        return sequence;
    }
}
