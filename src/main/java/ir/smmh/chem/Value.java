package ir.smmh.chem;

import ir.smmh.util.StringUtil;

import java.util.Set;

@SuppressWarnings("unused")
public interface Value {

    Set<Character> ALLOWED = StringUtil.characterSet(".0123456789");

    double getValue();

    int getSignificantFigures();

    int getDecimalPlaces();

    Value add(Value other);

    Value subtract(Value other);

    Value multiply(Value other);

    Value divide(Value other);
}
