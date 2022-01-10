package ir.smmh.util;

import ir.smmh.Backward;
import org.jetbrains.annotations.NotNull;

public interface StringUtil {
    byte RADIX_MAX = 36;
    byte RADIX_HEX = 16;
    byte RADIX_DEC = 10;
    byte RADIX_OCT = 8;
    byte RADIX_BIN = 2;

    static String tabIn(String string) {
        return shiftRight(string, 1, '\t');
    }

    static String shiftRight(String string, int length) {
        return shiftRight(string, length, ' ');
    }

    static String shiftRight(String string, int length, char ch) {
        final String prefix = repeat(ch, length);
        final StringBuilder builder = new StringBuilder();
        boolean notFirstTime = false;
        for (String line : string.split("\n")) {
            if (notFirstTime) {
                builder.append("\n");
            } else {
                notFirstTime = true;
            }
            builder.append(prefix);
            builder.append(line);
        }
        return builder.toString();
    }

    static String repeat(char ch, int count) {
        return Character.toString(ch).repeat(Math.max(0, count));
    }

    static byte valueOfSymbol(String s) {
        assert s.length() == 1;
        return valueOfSymbol(s.charAt(0));
    }

    static byte valueOfSymbol(char c, byte radix) {
        byte v = valueOfSymbol(c);
        if (v < radix)
            return v;
        else
            throw new IllegalArgumentException("symbol: " + c + " does not match radix: " + radix);
    }

    static char symbolOfValue(byte value, byte radix) {
        if (value < radix)
            return symbolOfValue(value);
        else
            throw new IllegalArgumentException("value: " + value + " does not match radix: " + radix);
    }

    /**
     * Assumes the given Unicode codepoint is either a digit or a letter and maps it
     * to its base-36 value. Both uppercase and lowercase characters are supported.
     * Any other codepoint will lead to unexpected results.
     */
    static byte valueOfSymbol(char c) {
        byte value = (byte) c;

        // handles digits
        value -= 48;

        // handles uppercase letters
        if (value >= 17)
            value -= 7;

        // handles lowercase letters
        if (value >= 42)
            value -= 32;

        return value;
    }

    /**
     * Converts {{@code 0}, {@code 1}, {@code 2}, ... {@code 9}, {@code 10},
     * {@code 11}, ... {@code 33}, {@code 34}, {@code 35}} as <i>byte</i> into
     * {{@code '0'}, {@code '1'}, {@code '2'}, ... {@code '9'}, {@code 'A'},
     * {@code 'B'}, ... {@code 'X'}, {@code 'Y'}, {@code 'Z'}} as <i>char</i>.
     */
    static char symbolOfValue(byte value) {
        assert value >= 0 && value < RADIX_MAX;

        // handles digits
        if (value < 10)
            return (char) (value + 48);

            // handles letters
        else
            return (char) (value + 55);
    }

    static String stringOfValue(int value) {
        return stringOfValue(value, RADIX_DEC);
    }

    static String stringOfValue(int value, byte radix) {

        StringBuilder string = new StringBuilder();

        while (value > 0) {
            string.insert(0, symbolOfValue((byte) (value % radix)));
            value /= radix;
        }

        if (string.length() == 0)
            return "0";
        else
            return string.toString();
    }

    /**
     * <table>
     * <tr>
     * <th>Input as {@code String}</th>
     * <th>Output as {@code Number}</th>
     * </tr>
     * <tr>
     * <td>"1234"</td>
     * <td>1234</td>
     * </tr>
     * <tr>
     * <td>"0"</td>
     * <td>0</td>
     * </tr>
     * <tr>
     * <td>""</td>
     * <td>0</td>
     * </tr>
     * </table>
     */
    static Number valueOfString(String string) {
        return valueOfString(string, RADIX_DEC);
    }

    /**
     * <table>
     * <tr>
     * <th>Input as {@code String}</th>
     * <th>Radix as {@code int}</th>
     * <th>Output as {@code Number}</th>
     * </tr>
     * <tr>
     * <td>"ffffff"</td>
     * <td>16</td>
     * <td>16777215</td>
     * </tr>
     * <tr>
     * <td>"11"</td>
     * <td>10</td>
     * <td>11</td>
     * </tr>
     * <tr>
     * <td>"11"</td>
     * <td>2</td>
     * <td>3</td>
     * </tr>
     * </table>
     */
    static Number valueOfString(@NotNull String string, byte radix) {

        if (string.length() == 0)
            throw new IllegalArgumentException("empty string has no numeric value");

        int point = string.indexOf('.');

        int whole;
        if (point == -1)
            whole = string.length();
        else
            whole = point;

        int value = 0;

        for (int i = 0; i < whole; i++) {
            value *= radix;
            value += valueOfSymbol(string.charAt(i));
        }

        if (point == -1) {

            // return int
            return value;
        } else {

            double subvalue = 0;

            for (int i = string.length() - 1; i > point; i--) {
                subvalue += valueOfSymbol(string.charAt(i));
                subvalue /= radix;
            }

            // return double
            return subvalue + value;
        }
    }

    static String fill(String input, String filler, boolean leftwards, int length, boolean cut) {
        int n = input.length();
        if (n > length) {
            if (cut) {
                if (leftwards) {
                    return input.substring(n - length);
                } else {
                    return input.substring(0, length);
                }
            } else {
                return input;
            }
        } else {
            String addition = Backward.repeat(filler, (int) Math.ceil((length - n) / (float) filler.length()));
            if (addition.length() > length) {
                addition = addition.substring(0, length);
            }
            if (leftwards) {
                return addition + input;
            } else {
                return input + addition;
            }
        }
    }

    static @NotNull String codepointToText(int codepoint) {
        return "U+" + fill(stringOfValue(codepoint, RADIX_HEX), "0", true, 4, false);
    }

    static int count(@NotNull String string, char c) {
        int count = 0;
        for (int i = 0; i < string.length(); i++)
            if (string.charAt(i) == c)
                count++;
        return count;
    }

    static @NotNull String[] split(@NotNull String string, char splitter) {

        int n = count(string, splitter) + 1;

        String[] array = new String[n];

        int a, b;

        a = 0;

        for (int i = 0; i < n; i++) {
            b = string.indexOf(splitter, a);
            if (b == -1)
                array[i] = string.substring(a);
            else
                array[i] = string.substring(a, b);
            a = b + 1;
        }

        return array;
    }
}
