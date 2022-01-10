package ir.smmh.lingu.groupermaker.impl;

import ir.smmh.lingu.Token;
import ir.smmh.lingu.groupermaker.GrouperMakerImpl;
import ir.smmh.lingu.settings.FormalSettings;

public class Metadata extends GrouperMakerImpl.DefinitionImpl {

    public final String[] exts;

    public final String root;

    public Metadata(FormalSettings src) {
        super(src.getName());

        // exts
        Token.Individual[] exts = src.getTokens("ext");
        this.exts = new String[exts.length];
        for (int i = 0; i < exts.length; i++)
            this.exts[i] = exts[i].getData();

        // root
        this.root = src.getSoleString("root", false);
    }

    @Override
    public int getPriority() {
        return 100;
    }
}
