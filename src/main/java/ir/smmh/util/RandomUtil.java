package ir.smmh.util;

import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;

/**
 * This interface is used to generate random things like strings or numbers.
 */
public interface RandomUtil {

    Random generate = new Random();

    /**
     * Generate a random string of lower case hex digits of given length.
     *
     * @param length Length of the generated string
     * @return A randomly generated string
     */
    @NotNull
    static String generateRandomHex(int length) {
        char[] array = new char[length];
        for (int i = 0; i < length; i++) {
            int x = generate.nextInt(16);
            array[i] = (char) (x < 10 ? '0' + x : 'a' + x - 10);
        }
        return new String(array);
    }

    static int[] generateRandomIntArray(int count, int bound) {
        int[] array = new int[count];
        for (int i = 0; i < count; i++)
            array[i] = generate.nextInt(bound);
        return array;
    }

    static <T> T chooseRandomly(Set<T> choices) {
        int size = choices.size();
        if (size == 0) throw new UnsupportedOperationException("cannot choose randomly from empty set");
        int i = generate.nextInt(size);
        for (T t : choices) if (i-- == 0) return t;
        return null;
    }
}
