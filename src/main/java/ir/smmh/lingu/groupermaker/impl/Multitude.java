package ir.smmh.lingu.groupermaker.impl;

import ir.smmh.jile.common.Range;
import ir.smmh.lingu.groupermaker.GrouperMakerImpl;
import ir.smmh.lingu.settings.FormalSettings;

public class Multitude extends GrouperMakerImpl.DefinitionImpl {

    public final Range.Integer count;

    public final Boolean opaque;

    public final String separator, opener, closer;

    public final Boolean ignore;

    public Multitude(FormalSettings src) {

        super(src.getName());

        count = src.getRange("count");

        opaque = src.getBoolean("opaque");

        separator = src.getSoleString("separator", true);

        opener = src.getSoleString("starts-with", true);

        closer = src.getSoleString("ends-with", true);

        ignore = src.getBoolean("ignore");

    }

    @Override
    public int getPriority() {
        return 20;
    }
}
