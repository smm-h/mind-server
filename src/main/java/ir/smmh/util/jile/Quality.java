package ir.smmh.util.jile;

// import static ir.smmh.jile.common.Quality.*; // you are going to need this

/**
 * <a href=https://docs.oracle.com/javase/tutorial/java/generics/wildcardGuidelines.html></a>
 */
public interface Quality<T> {

    static <T> boolean is(T o, Quality<? super T> q) {
        return q.holdsFor(o);
    }

    static <T> boolean isNot(T o, Quality<? super T> q) {
        return !q.holdsFor(o);
    }

    static <T> Quality<T> not(Quality<T> q) {
        return o -> !q.holdsFor(o);
    }

    static <T> Quality<T> and(Quality<T> q, Quality<T> r) {
        return o -> q.holdsFor(o) && r.holdsFor(o);
    }

    static <T> Quality<T> or(Quality<T> q, Quality<T> r) {
        return o -> q.holdsFor(o) || r.holdsFor(o);
    }

    boolean holdsFor(T o);
}