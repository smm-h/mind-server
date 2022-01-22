package ir.smmh.lingu.settings;

import ir.smmh.jile.common.Range;
import ir.smmh.lingu.CodeProcess;
import ir.smmh.lingu.Token;

import java.util.function.Predicate;

public interface FormalSettings {
    Predicate<FormalSettings> COMPLETE = FormalSettings::isComplete;

    CodeProcess getWrapping();

    InformalSettings getSrc();

    String getName();

    FormalizationBlueprint getType();

    boolean isComplete();

    Number getSoleNumeric(String key);

    Number getNumeric(String key, int index);

    Boolean getBoolean(String key);

    Range.Integer getRange(String key);

    String getSoleString(String key, boolean escape);

    String getStringAt(String key, int index, boolean escape);

    Token.Individual getTokenAt(String key, int index);

    Token.Individual[] getTokens(String key);

    int getActualLengthOf(String key);

    Length getValidLengthOf(String key);

    String getIdentity();

    String getRepresentation();
}
