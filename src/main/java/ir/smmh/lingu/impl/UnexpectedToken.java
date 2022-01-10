package ir.smmh.lingu.impl;

import ir.smmh.lingu.Token;

public class UnexpectedToken extends MishapImpl.Caused {
    private final String expectation, extraPosition;

    public UnexpectedToken(Token unexpectedToken) {
        super(unexpectedToken.getFirstHandle(), true);
        this.expectation = "";
        this.extraPosition = "";
    }

    public UnexpectedToken(Token unexpectedToken, String expectation) {
        super(unexpectedToken.getFirstHandle(), true);
        this.expectation = "; instead was expecting: `" + expectation + "`";
        this.extraPosition = "";
    }

    public UnexpectedToken(Token unexpectedToken, String expectation, Token next) {
        super(unexpectedToken.getLastHandle(), true);
        this.expectation = "; instead was expecting: `" + expectation + "`";
        this.extraPosition = " before `" + next.getData() + "`";
    }

    public UnexpectedToken(Token prev, Token unexpectedToken, String expectation) {
        super(unexpectedToken.getFirstHandle(), true);
        this.expectation = "; instead was expecting: `" + expectation + "`";
        this.extraPosition = " after `" + prev.getData() + "`";
    }

    public String getReport() {
        return "Unexpected `" + token.getData() + "`" + extraPosition + expectation;
    }
}
