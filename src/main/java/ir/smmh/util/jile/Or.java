package ir.smmh.util.jile;

/**
 * The base class for "this or that" object, which holds exactly one object of
 * type either This or That.
 */
public interface Or<This, That> {

    Or<This, That> makeThis(This a);

    Or<This, That> makeThat(That b);

    boolean isThis();

    boolean isThat();

    This getThis();

    That getThat();

}
