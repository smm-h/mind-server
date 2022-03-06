package ir.smmh.util;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanClone;
import ir.smmh.util.impl.FormImpl;
import ir.smmh.util.jile.Or;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A form is a plain text document with spaces to fill out or leave blank. It is
 * useful for generating end-texts from patterns. Some spaces may accept more
 * than one value. Start with StringForm.empty() and chain methods to build your
 * form, and finish with toString(). Be advised that all filling out method
 * mutate the form meaning you will not be able to use it more than once.
 * To avoid this, use fillAndWrite() which does not mutate the form.
 */
public interface Form extends CanClone<Form>, Mutable {

    static @NotNull Form blank() {
        return new FormImpl();
    }

    /**
     * This method helps you use a pre-made string form, fill it, and write its
     * contents to a file. However, it does not actually mutate the source form
     * like all other fill methods.
     *
     * @throws UnsupportedOperationException If filling out the form fails
     * @throws IOException                   If writing to file fails
     */
    static void fillAndWrite(Form form, Map.MultiValue<BlankSpace, String> map, Path destination) throws IOException {
        Form clone = form.clone(false);
        clone.fillOut(map);
        Files.writeString(destination, clone.toString());
    }

    boolean isFilledOut();

    @Contract("_->this")
    @NotNull Form append(BlankSpace blankSpace);

    @Contract("_->this")
    @NotNull Form prepend(BlankSpace blankSpace);

    @Contract("_->this")
    @NotNull Form append(Form form);

    @Contract("_->this")
    @NotNull Form prepend(Form form);

    @Contract("_->this")
    @NotNull Form append(String text);

    @Contract("_->this")
    @NotNull Form prepend(String text);

    @Contract("_->this")
    @NotNull Form append(char c);

    @Contract("_->this")
    @NotNull Form prepend(char c);

    @Contract("_->this")
    default @NotNull Form leaveBlank(BlankSpace blankSpace) {
        fillOut(blankSpace, Sequential.empty());
        return this;
    }

    @Contract("_, _->this")
    @NotNull Form fillOut(BlankSpace blankSpace, Sequential<String> values);

    @Contract("_, _->this")
    default @NotNull Form fillOut(BlankSpace blankSpace, String... values) {
        fillOut(blankSpace, Sequential.of(values));
        return this;
    }

    @Contract("_->this")
    @NotNull Form fillOut(Map.MultiValue<BlankSpace, String> map);

    @NotNull Sequential<Or<String, BlankSpace>> getSequence();

    interface BlankSpace {

        boolean isInexhaustible();

        BlankSpace ITSELF = (Form.BlankSpace.ExactlyOne) Sequential::getSingleton;

        boolean acceptsLength(int length);

        @NotNull String enterValues(Sequential<String> values);

        default @NotNull String leaveBlank() {
            return enterValues(Sequential.empty());
        }

        @FunctionalInterface
        interface ZeroOrMore extends BlankSpace {
            @Override
            default boolean acceptsLength(int length) {
                return length >= 0;
            }

            @Override
            default boolean isInexhaustible() {return true;}
        }

        @FunctionalInterface
        interface OneOrMore extends BlankSpace {
            @Override
            default boolean acceptsLength(int length) {
                return length >= 1;
            }

            @Override
            default boolean isInexhaustible() {return true;}
        }

        @FunctionalInterface
        interface ZeroOrOne extends BlankSpace {
            @Override
            default boolean acceptsLength(int length) {
                return length == 0 || length == 1;
            }

            @Override
            default boolean isInexhaustible() {return false;}
        }

        @FunctionalInterface
        interface ExactlyOne extends BlankSpace {
            @Override
            default boolean acceptsLength(int length) {
                return length == 1;
            }

            @Override
            default boolean isInexhaustible() {return false;}
        }
    }
}
