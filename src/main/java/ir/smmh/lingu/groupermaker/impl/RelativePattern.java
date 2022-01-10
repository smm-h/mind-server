package ir.smmh.lingu.groupermaker.impl;

import ir.smmh.lingu.groupermaker.GrouperMakerImpl;
import ir.smmh.lingu.settings.FormalSettings;

public class RelativePattern extends GrouperMakerImpl.DefinitionImpl {
    public RelativePattern(FormalSettings src) {
        super(src.getName());
    }

    @Override
    public int getPriority() {
        return 40;
    }
}
