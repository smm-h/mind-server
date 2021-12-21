package ir.smmh.util;

public interface StringUtil {
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
}
