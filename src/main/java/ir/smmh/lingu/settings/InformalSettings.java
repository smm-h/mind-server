package ir.smmh.lingu.settings;

import ir.smmh.lingu.Token;

public interface InformalSettings {
    Token.Individual[] get(String key);

    Token.Individual getName();

    Token.Individual getType();

    String getIdentity();

    boolean sets(String key);

    void set(String key, Token.Individual... value);
}
