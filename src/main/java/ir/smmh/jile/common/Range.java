package ir.smmh.jile.common;

public class Range {

    public static class Integer extends Range {

        public static final int INFINITY = java.lang.Integer.MAX_VALUE;

        private final int min, max;

        public Integer(int min, int max) {
            super();
            if (min > max)
                throw new IllegalArgumentException();
            this.min = min;
            this.max = max;
        }

        public final boolean contains(int number) {
            return number >= min && number <= max;
        }
    }
}