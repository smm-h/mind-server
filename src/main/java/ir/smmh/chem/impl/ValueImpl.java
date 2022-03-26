package ir.smmh.chem.impl;

import ir.smmh.chem.Value;
import ir.smmh.util.StringUtil;
import ir.smmh.util.jile.MathUtil;

public class ValueImpl implements Value {
    private final double value;
    private final int significantFigures, decimalPlaces;

    public ValueImpl(String string) {
        int n = string.length();
        assert n > 0;
        for (char c : StringUtil.characterSet(string))
            assert ALLOWED.contains(c);
        assert StringUtil.count(string, '.') <= 1;
        value = Double.parseDouble(string);
        int p = string.indexOf('.');
        int start = -1;
        for (int i = 0; i < n; i++) {
            char c = string.charAt(i);
            if (c != '.' && c != '0') {
                start = i;
                break;
            }
        }
        int end;
        if (p == -1) {
            end = -1;
            for (int i = n - 1; i >= 0; i--) {
                char c = string.charAt(i);
                if (c != '.' && c != '0') {
                    end = i;
                    break;
                }
            }
            decimalPlaces = 0;
        } else {
            if (start > p) start--;
            end = n - 2;
            decimalPlaces = n - 1 - p;
        }
        significantFigures = end - start + 1;
    }

    private ValueImpl(double value, int significantFigures, int decimalPlaces) {
        for (int i = 0; i < decimalPlaces; i++) value *= 10;
        value = Math.round(value);
        for (int i = 0; i < decimalPlaces; i++) value /= 10;
        this.value = value;
        this.significantFigures = significantFigures;
        this.decimalPlaces = decimalPlaces;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public int getSignificantFigures() {
        return significantFigures;
    }

    @Override
    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    @Override
    public Value add(Value other) {
        double v = value + other.getValue();
        int dp = Math.min(decimalPlaces, other.getDecimalPlaces());
        int sf = MathUtil.countDigits(MathUtil.floor(v)) + dp;
        return new ValueImpl(v, sf, dp);
    }

    @Override
    public Value subtract(Value other) {
        double v = value - other.getValue();
        int dp = Math.min(decimalPlaces, other.getDecimalPlaces());
        int sf = MathUtil.countDigits(MathUtil.floor(v)) + dp;
        return new ValueImpl(v, sf, dp);
    }

    @Override
    public Value multiply(Value other) {
        double v = value * other.getValue();
        int sf = Math.min(significantFigures, other.getSignificantFigures());
        int dp = sf - MathUtil.countDigits(MathUtil.floor(v));
        return new ValueImpl(v, sf, dp);
    }

    @Override
    public Value divide(Value other) {
        double v = value / other.getValue();
        int sf = Math.min(significantFigures, other.getSignificantFigures());
        int dp = sf - MathUtil.countDigits(MathUtil.floor(v));
        return new ValueImpl(v, sf, dp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Value)) return false;

        Value other = (Value) o;

        return significantFigures == other.getSignificantFigures()
                && decimalPlaces == other.getDecimalPlaces()
                && value == other.getValue();
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public String toString() {
        return value + " [" + significantFigures + "/" + decimalPlaces + "]";
    }
}
