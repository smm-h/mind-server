package ir.smmh.lingu.impl;

import ir.smmh.lingu.Code;
import ir.smmh.lingu.Maker;
import ir.smmh.lingu.groupermaker.impl.Formalizer;
import ir.smmh.lingu.processors.impl.MultiprocessorImpl;
import ir.smmh.lingu.settings.Settings;
import ir.smmh.lingu.settings.impl.SettingsImpl;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.Objects;

public class GrouperMaker extends LanguageImpl {

    private static GrouperMaker singleton;
    private final Formalizer formalizer;
    public final Maker<GrouperImpl> maker = code -> {

        return new GrouperImpl(Objects.requireNonNull(GrouperMaker.this.formalizer.maker.makeFromCode(code)));

        // System.out.println(language.name + ":");
        // System.out.println("\t<+ " + key);

        // port.write(code, grouper);

        // return grouper;
    };

    private GrouperMaker() throws FileNotFoundException, Maker.MakingException {
        super("Grouper Maker", "ncx", new MultiprocessorImpl());
        formalizer = new Formalizer();
        getProcessor().extend(formalizer.getProcessor());
    }

    // public static final Port<Grouper> port = new Port<Grouper>();

    public static GrouperMaker singleton() throws FileNotFoundException, Maker.MakingException {
        if (singleton == null) {
            singleton = new GrouperMaker();
        }
        return singleton;
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