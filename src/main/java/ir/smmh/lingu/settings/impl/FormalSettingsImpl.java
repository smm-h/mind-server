package ir.smmh.lingu.settings.impl;

import ir.smmh.jile.common.Range;
import ir.smmh.lingu.CodeProcess;
import ir.smmh.lingu.Token;
import ir.smmh.lingu.impl.TokenizerMakerImpl;
import ir.smmh.lingu.settings.FormalSettings;
import ir.smmh.lingu.settings.FormalizationBlueprint;
import ir.smmh.lingu.settings.InformalSettings;
import ir.smmh.lingu.settings.Length;
import ir.smmh.lingu.settings.err.InvalidLength;
import ir.smmh.lingu.settings.err.InvalidValue;
import ir.smmh.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class FormalSettingsImpl implements FormalSettings {

    private final InformalSettings src;
    private final String name;
    private final FormalizationBlueprint type;
    private final Token.Individual[][] values;
    private final boolean complete;

    private CodeProcess wrapping;

    public FormalSettingsImpl(InformalSettings src, String name, FormalizationBlueprint type, Token.Individual[][] absolvedValues, boolean complete) {
        this.src = src;
        this.name = name;
        this.type = type;
        this.values = absolvedValues;
        this.complete = complete;
    }

    @Override
    public CodeProcess getWrapping() {
        return wrapping;
    }

    @Override
    public InformalSettings getSrc() {
        return src;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public FormalizationBlueprint getType() {
        return type;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public Number getSoleNumeric(String key) {
        return StringUtil.valueOfString(getSoleString(key, false));
    }

    @Override
    public Number getNumeric(String key, int index) {
        return StringUtil.valueOfString(getStringAt(key, index, false));
    }

    @Override
    public Boolean getBoolean(String key) {
        String value = getSoleString(key, false);
        switch (value) {
            case "true":
                return true;
            case "false":
                return false;
            default:
                wrapping.issue(new InvalidValue(getTokenAt(key, 0)));
                return null;
        }
    }

    @Override
    public Range.Integer getRange(String key) {

        int min, max;

        // extract
        switch (getActualLengthOf(key)) {
            case 1:
                min = max = (int) getSoleNumeric(key);
                break;
            case 2:
                min = (int) getNumeric(key, 0);
                max = (int) getNumeric(key, 1);
                break;
            default:
                wrapping.issue(new InvalidLength(this, key));
                return null;
        }

        // validate
        if (max != 0 && (min == 0 || min > max)) {
            wrapping.issue(new InvalidValue(getTokenAt(key, 1)));
            return null;
        }

        if (min == 0)
            min = Range.Integer.INFINITY;

        if (max == 0)
            max = Range.Integer.INFINITY;

        return new Range.Integer(min, max);
    }

    @Override
    public String getSoleString(String key, boolean escape) {
        if (getActualLengthOf(key) == 1) {
            return getStringAt(key, 0, escape);
        } else {
            wrapping.issue(new InvalidLength(this, key));
            return null;
        }
    }

    @Override
    public String getStringAt(String key, int index, boolean escape) {
        String s = getTokens(key)[index].getData();
        if (escape)
            return TokenizerMakerImpl.escape(s);
        else
            return s;
    }

    @Override
    public Token.Individual getTokenAt(String key, int index) {
        return getTokens(key)[index];
    }

    @Override
    public Token.Individual[] getTokens(String key) {
        return values[type.getIndex(key)];
    }

    @Override
    public int getActualLengthOf(String key) {
        return values[type.getIndex(key)].length;
    }

    @Override
    public Length getValidLengthOf(String key) {
        return type.getValidLength(type.getIndex(key));
    }

    @Override
    public String getIdentity() {
        return type.getName() + " '" + name + "'";
    }

    @NotNull
    @Override
    public String toString() {
        return getIdentity();
    }

    @Override
    public String getRepresentation() {
        StringBuilder builder = new StringBuilder(name);
        if (type != null) {
            builder.append(" as ").append(type.getName()).append(" = {");
            for (int index = 0; index < values.length; index++) {
                if (index > 0)
                    builder.append(", ");
                builder.append(type.getKey(index)).append(": ").append(Arrays.toString(values[index]));
            }
            builder.append("}");
        }
        return builder.toString();
    }
}
