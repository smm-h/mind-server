package ir.smmh.lingu.groupermaker.impl;

import ir.smmh.lingu.impl.GrouperMaker;
import ir.smmh.lingu.settings.FormalSettings;

public class RelativePattern extends GrouperMaker.DefinitionImpl {
    public RelativePattern(FormalSettings src) {
        super(src.getName());
    }

    @Override
    public int getPriority() {
        return 40;
    }
}
