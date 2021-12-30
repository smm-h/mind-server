package ir.smmh.lingu;

import ir.smmh.lingu.impl.Port;
import ir.smmh.tree.jile.Tree;

import java.util.List;

public interface Token {

    Port<Tree<Token>> tree = new Port<>("Token:tree");

    default String getTypeString() {
        return getType().toString();
    }

    default boolean is(String type) {
        return getTypeString().equals(type);
    }

    default boolean is(String... types) {
        for (String type : types) {
            if (is(type)) {
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

    Individual getFirstHandle();

    Individual getLastHandle();

    Type getType();

    int getPosition();

    String getData();

    interface Individual extends Token {
    }

    interface Collective extends Token {
        List<Token> getChildren();

        Individual getOpener();

        Individual getCloser();
    }

    interface Type {

        interface Individual extends Type {
        }

        interface Collective extends Type {
        }
    }
}
