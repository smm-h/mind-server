package ir.smmh.lingu.groupermaker;

import ir.smmh.lingu.Code;
import ir.smmh.lingu.Grouper;
import ir.smmh.lingu.GrouperMaker;
import ir.smmh.lingu.Maker;
import ir.smmh.lingu.groupermaker.impl.Formalizer;
import ir.smmh.lingu.impl.GrouperImpl;
import ir.smmh.lingu.impl.LanguageImpl;
import ir.smmh.lingu.processors.impl.MultiprocessorImpl;
import ir.smmh.lingu.settings.Settings;
import ir.smmh.lingu.settings.impl.SettingsImpl;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.Objects;

public class GrouperMakerImpl extends LanguageImpl implements GrouperMaker {

    private final Formalizer formalizer;

    public GrouperMakerImpl() throws FileNotFoundException, Maker.MakingException {
        super("Grouper Maker", "ncx", new MultiprocessorImpl());
        formalizer = new Formalizer();
        getProcessor().extend(formalizer.getProcessor());
    }

    @Override
    public @NotNull Grouper makeFromCode(@NotNull Code code) throws MakingException {

        return new GrouperImpl(GrouperMakerImpl.this.formalizer.maker.makeFromCode(code));

        // System.out.println(language.name + ":");
        // System.out.println("\t<+ " + key);

        // port.write(code, grouper);

        // return grouper;
    }

    public interface Definition extends Settings {
        int getPriority();
    }

    public abstract static class DefinitionImpl extends SettingsImpl implements Definition {
        public DefinitionImpl(String name) {
            super(name);
        }
    }

}