package ir.smmh.util.jile;

public interface MathUtil {

    static double factorial(double n) {
        return n == 0 ? 1 : n * factorial(n - 1);
    }

    static long factorial(int n) {
        return n == 0 ? 1 : n * factorial(n - 1);
    }

    static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    static int tens(double x) {
        return tens(x, 0);
    }

    static int tens(double x, int depth) {
        return is_int(x) ? depth : tens(x * 10, depth + 1);
    }

    static int floor(double x) {
        return (int) x;
    }

    static int ceil(double x) {
        int f = floor(x);
        return f == x ? f : f + 1;
    }

    static int round(double x) {
        int f = floor(x);
        return (x - f) < 0.5 ? f : f + 1;
    }

    static boolean is_int(double x) {
        return floor(x) == x;
    }

    static float sqrt(float x) {
        return (float) Math.sqrt(x);
    }

    static double sqrt(double x) {
        return Math.sqrt(x);
    }

    static float sqrt(int x) {
        return (float) Math.sqrt(x);
    }

    static int sqr(int x) {
        return x * x;
    }

    static double sqr(double x) {
        return x * x;
    }

    static long power(int b, int p) {
        long x = 1;
        for (int i = 0; i < p; i++)
            x *= b;
        return x;
    }

    static double power(double b, double p) {
        return Math.pow(b, p);
    }

    static double log(double n, double b) {
        return Math.log(n) / Math.log(b);
    }

    static boolean isPowerOf(double n, double b) {
        return is_int(log(n, b));
    }

    static float distance(int x1, int y1, int x2, int y2) {
        return sqrt(sqr(x1 - x2) + sqr(y1 - y2));
    }

    static double distance(double x1, double y1, double x2, double y2) {
        return sqrt(sqr(x1 - x2) + sqr(y1 - y2));
    }

    static int sign(int n) {
        return Integer.compare(n, 0);
    }

    static int sum(int n) {
        return n * (n + 1) / 2;
    }
}
