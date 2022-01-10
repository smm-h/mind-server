package ir.smmh.lingu.settings.err;

import ir.smmh.lingu.Token;
import ir.smmh.lingu.impl.MishapImpl;

public class InvalidValue extends MishapImpl.Caused {

    private final String validValues;

    public InvalidValue(Token.Individual token) {
        this(token, null);
    }

    public InvalidValue(Token.Individual token, String validValues) {
        super(token, true);
        this.validValues = validValues;
    }

    @Override
    public String getReport() {
        String string = "Invalid value: `" + token.getData() + "`";
        if (validValues != null)
            string += ", valid values are: `" + validValues + "`";
        return string;
    }
}
