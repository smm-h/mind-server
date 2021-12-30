package ir.smmh.lingu;

import ir.smmh.lingu.IndividualTokenType.IndividualToken;
import ir.smmh.tree.jile.Tree;

public interface Token {

    default String getTypeString() {
        return getType().toString();
    }

    default boolean is(String type) {
        return getTypeString().equals(type);
    }

    default boolean is(String... types) {
        for (int i = 0; i < types.length; i++) {
            if (is(types[i])) {
                return true;
            }
        }
        return false;
    }

    default boolean is(Iterable<String> types) {
        for (String type : types) {
            if (is(type)) {
                return true;
            }
        }
        return false;
    }

    IndividualToken getFirstHandle();

    IndividualToken getLastHandle();

    Port<Tree<Token>> tree = new Port<Tree<Token>>("Token:tree");

    TokenType getType();

    Integer getPosition();

    String getData();
}
