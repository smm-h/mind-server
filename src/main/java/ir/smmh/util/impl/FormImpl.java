package ir.smmh.util.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.util.Form;
import ir.smmh.util.Map;
import ir.smmh.util.Mutable;
import ir.smmh.util.jile.Or;
import ir.smmh.util.jile.impl.FatOr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FormImpl implements Form, Mutable.WithListeners.Injected {
    private final String title;
    private final Map.MultiValue.Mutable<BlankSpace, String> map;
    private final Sequential.Mutable.VariableSize<Or<String, BlankSpace>> sequence;
    private transient String string = null;
    private transient boolean isFilledOut = false;

    public FormImpl(String title) {
        this(title, new SequentialImpl<>(), new MapImpl.MultiValue.Mutable<>());
    }

    private FormImpl(String title, Sequential.Mutable.VariableSize<Or<String, BlankSpace>> sequence, Map.MultiValue.Mutable<BlankSpace, String> map) {
        this.title = title;
        this.sequence = sequence;
        this.map = map;
        getOnCleanListeners().add(() -> {
            isFilledOut = false;
            StringBuilder builder = new StringBuilder();
            int n = sequence.getSize();
            for (int i = 0; i < n; i++) {
                var thisOrThat = sequence.getAtIndex(i);
                if (thisOrThat.isThis()) {
                    builder.append(thisOrThat.getThis());
                } else {
                    BlankSpace blankSpace = thisOrThat.getThat();
                    Sequential<String> values = map.getAtPlace(blankSpace);
                    int count = values.getSize();
                    if (blankSpace.acceptsCount(count))
                        builder.append(blankSpace.compose(values));
                    else
                        throw new IncompleteFormException(blankSpace, blankSpace.countErrorMessage(count));
                }
            }
            string = builder.toString();
            isFilledOut = true;
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
        return copy("clone of " + title);
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
    public void generateToFile(Path destination, boolean overwrite) throws IOException {
        if (overwrite || !Files.exists(destination)) {
            Files.writeString(destination, generate());
        }
    }

    @Override
    public @NotNull Form enter(BlankSpace blankSpace, @Nullable String entry) {
        if (entry != null) map.addAtPlace(blankSpace, entry);
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
    public @NotNull String generate() {
        if (isFilledOut()) return string;
        throw new IncompleteFormException("the form is not filled out");
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
