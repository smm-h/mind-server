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
    private final String title;
    private final Map.MultiValue.Mutable<BlankSpace, String> map;
    private final Sequential.Mutable.VariableSize<Or<String, BlankSpace>> sequence;
    private transient String string = null;
    private transient boolean isFilledOut = false;
    private transient IncompleteFormException lazyException;

    public FormImpl(String title) {
        this(title, new SequentialImpl<>(), new MapImpl.MultiValue.Mutable<>());
    }

    private FormImpl(String title, Sequential.Mutable.VariableSize<Or<String, BlankSpace>> sequence, Map.MultiValue.Mutable<BlankSpace, String> map) {
        this.title = title;
        this.sequence = sequence;
        this.map = map;
        getOnCleanListeners().add(() -> {
            isFilledOut = false;
            lazyException = null;
            StringBuilder b = new StringBuilder();
            int n = sequence.getSize();
            for (int i = 0; i < n; i++) {
                var o = sequence.getAtIndex(i);
                if (o.isThis()) {
                    b.append(o.getThis());
                } else {
                    BlankSpace s = o.getThat();
                    try {
                        b.append(s.compose(map.getAtPlace(s)));
                    } catch (IncompleteFormException e) {
                        lazyException = e;
                        return;
                    }
                }
            }
            string = b.toString();
            isFilledOut = true;
        });
    }

    @Override
    public Form clone(boolean deepIfPossible) {
        return copy("clone of " + title);
    }

    private static Or<String, BlankSpace> make(String string) {
        return FatOr.makeThis(string);
    }

    private static Or<String, BlankSpace> make(BlankSpace blankSpace) {
        return FatOr.makeThat(blankSpace);
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
    public @NotNull Form copy(String title) {
        return new FormImpl(title, sequence.clone(false), map.clone(false));
    }

    @Override
    public @NotNull Form enter(BlankSpace blankSpace, String entry) {
        map.addAtPlace(blankSpace, entry);
        return this;
    }

    @Override
    public @NotNull Form enter(BlankSpace blankSpace, Sequential<String> entries) {
        map.addAllAtPlace(blankSpace, entries);
        return this;
    }

    @Override
    public @NotNull Sequential<Or<String, BlankSpace>> getSequence() {
        return sequence;
    }

    @Override
    public @NotNull Form enter(Map.MultiValue<BlankSpace, String> mappedEntries) {
        map.addAllFrom(mappedEntries);
        return this;
    }

    @Override
    public @NotNull String generate() throws IncompleteFormException {
        if (isFilledOut())
            return string;
        else if (lazyException != null)
            throw lazyException;
        else
            throw new IncompleteFormException();
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    @Override
    public boolean isFilledOut() {
        return clean() && isFilledOut;
    }

    @Override
    public String toString() {
        return isFilledOut() ? string : "INCOMPLETE FORM";
    }

    @Override
    public @NotNull Mutable.WithListeners getInjectedMutable() {
        return sequence;
    }
}
