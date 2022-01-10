package ir.smmh.lingu.settings.err;

import ir.smmh.lingu.Token;
import ir.smmh.lingu.impl.MishapImpl;

public class Unbalanced extends MishapImpl.Caused {
    public Unbalanced(Token.Individual token) {
        super(token, true);
    }

    @Override
    public String getReport() {
        return "Unbalanced groups";
    }
}
