package ir.smmh.lingu.settings;

import ir.smmh.lingu.Token;

public interface FormalizationBlueprint {
    void defineBlueprint();

    String getName();

    void add(String key, Token.Individual... defaultValue);

    void addRange(String key);

    void addBoolean(String key, boolean defaultValue);

    void add(String key, Length length, Token.Individual... defaultValue);

    Token.Individual[] getDefaultValue(int index);

    Length getValidLength(int index);

    String getKey(int index);

    int getIndex(String key);

    int size();

    boolean doesNameMatter();
}
