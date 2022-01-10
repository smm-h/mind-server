package ir.smmh.lingu.settings.err;

import ir.smmh.lingu.impl.MishapImpl;
import ir.smmh.lingu.settings.InformalSettings;

public class BothPrefixAndSuffix extends MishapImpl.Caused {

    private final InformalSettings src;

    public BothPrefixAndSuffix(InformalSettings subj) {
        super(subj.getName(), false);
        this.src = subj;
    }

    @Override
    public String getReport() {
        return "`" + src.getIdentity() + "` asks to be both prefixed and suffixed. Remove one of the dashes at the start or end of its name.";
    }
}
