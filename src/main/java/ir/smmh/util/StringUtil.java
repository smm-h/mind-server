package ir.smmh.util;

import ir.smmh.Backward;
import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
@ParametersAreNonnullByDefault
public interface StringUtil {
    byte RADIX_MAX = 36;
    byte RADIX_HEX = 16;
    byte RADIX_DEC = 10;
    byte RADIX_OCT = 8;
    byte RADIX_BIN = 2;
    char DECIMAL_POINT = '.';
    String LINEBREAK = "\n";
    char SPACE = ' ';
    char TAB = '\t';

    static String tabIn(String string) {
        return shiftRight(string, 1, TAB);
    }

    static String shiftRight(String string, int length) {
        return shiftRight(string, length, SPACE);
    }

    static String shiftRight(String string, int length, char ch) {
        String prefix = repeat(ch, length);
        StringBuilder builder = new StringBuilder(string.length() * 2);
        boolean firstTime = true;
        for (String line : string.split(LINEBREAK)) {
            if (firstTime) {
                firstTime = false;
            } else {
                builder.append(LINEBREAK);
            }
            builder.append(prefix);
            builder.append(line);
        }
        return builder.toString();
    }

    static String repeat(char ch, int count) {
        return Backward.repeat(Character.toString(ch), count);
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
        if (value < 10)
            // handles digits
            return (char) (value + 48);
        else if (value < RADIX_MAX)
            // handles letters
            return (char) (value + 55);
        else
            throw new IllegalArgumentException("value: " + value + " does not have a symbol");
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
     * <table summary="Examples for values of a given string in radix 10">
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
     * <table summary="Examples for values of a given string in various radices">
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
    @SuppressWarnings("SpellCheckingInspection")
    static Number valueOfString(String string, byte radix) {

        if (string.isEmpty())
            throw new IllegalArgumentException("empty string has no numeric value");

        int point = string.indexOf(DECIMAL_POINT);

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

            double subValue = 0;

            for (int i = string.length() - 1; i > point; i--) {
                subValue += valueOfSymbol(string.charAt(i));
                subValue /= radix;
            }

            // return double
            return subValue + value;
        }
    }

    /**
     * Takes an input string, applies some filling to it, and returns it
     *
     * @param input     The input string
     * @param filler    The string by which filling is to be done
     * @param leftwards The direction in which the filling is to be done
     * @param length    Length of the final string
     * @param cut       Pass true if keeping the output length is more important
     *                  than displaying the entire input
     * @return The output string
     */
    static String fill(String input, String filler, boolean leftwards, int length, boolean cut) {
        int n = input.length();
        if (n > length) {
            return cut ? (leftwards ? input.substring(n - length) : input.substring(0, length)) : input;
        } else {
            String addition = Backward.repeat(filler, (int) Math.ceil((length - n) / (float) filler.length()));
            if (addition.length() > length) addition = addition.substring(0, length);
            return leftwards ? addition + input : input + addition;
        }
    }

    static @NotNull String codepointToText(int codepoint) {
        return "U+" + fill(stringOfValue(codepoint, RADIX_HEX), "0", true, 4, false);
    }

    static boolean contains(String string, char c) {
        for (int i = 0; i < string.length(); i++)
            if (string.charAt(i) == c)
                return true;
        return false;
    }

    static int count(String string, char c) {
        int count = 0;
        for (int i = 0; i < string.length(); i++)
            if (string.charAt(i) == c)
                count++;
        return count;
    }

    static @NotNull Sequential<String> splitByLength(String string, int limit) {
        int l = string.length();
        int n = l / limit + (l % limit > 0 ? 1 : 0);
        String[] array = new String[n];

        for (int i = 0; i < n; i += 1) {
            int a = i * limit;
            if (a > l - limit) {
                array[i] = string.substring(a);
            } else {
                array[i] = string.substring(a, a + limit);
            }
        }

        return Sequential.of(array);
    }

    static @NotNull Sequential<String> splitByCharacter(String string, char splitter) {

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

        return Sequential.of(array);
    }

    static @NotNull Set<Character> characterSet(String chars) {
        Set<Character> set = new HashSet<>();
        for (char c : chars.toCharArray())
            set.add(c);
        return set;
    }

    static @NotNull String replaceCharacter(String string, char c, char replaceWith) {
        StringBuilder b = new StringBuilder(string);
        int n = b.length();
        for (int i = 0; i < n; i++) {
            if (b.charAt(i) == c) {
                b.setCharAt(i, replaceWith);
            }
        }
        return b.toString();
    }

    static String removePrefix(String string, String prefix) {
        return string.substring(prefix.length());
    }
}
