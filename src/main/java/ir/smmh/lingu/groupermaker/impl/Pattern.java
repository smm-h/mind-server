package ir.smmh.lingu.groupermaker.impl;

import ir.smmh.lingu.Token;
import ir.smmh.lingu.groupermaker.GrouperMakerImpl;
import ir.smmh.lingu.impl.TokenizerImpl;
import ir.smmh.lingu.settings.FormalSettings;

public class Pattern extends GrouperMakerImpl.DefinitionImpl {

    public final Boolean precedence;

    public final String[] pattern;
    public final boolean[] isVerbatim;

    public Pattern(FormalSettings src) {
        super(src.getName());

        precedence = src.getBoolean("precedence");

        int length = src.getActualLengthOf("pattern");

        pattern = new String[length];
        isVerbatim = new boolean[length];

        for (int i = 0; i < length; i++) {
            Token.Individual token = src.getTokenAt("pattern", i);
            pattern[i] = token.getData();
            isVerbatim[i] = token.getType() instanceof TokenizerImpl.Kept;
        }
    }

    @Override
    public int getPriority() {
        return 30;
    }
}
