package ir.smmh.util;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanClone;
import ir.smmh.util.impl.FormImpl;
import ir.smmh.util.jile.Or;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

/**
 * A form is a plain text document with spaces to fill out or leave blank. It is
 * useful for generating end-texts from patterns. Some spaces may accept more
 * than one value. Start with StringForm.empty() and chain methods to build your
 * form, and finish with toString(). Be advised that all filling out method
 * mutate the form meaning you will not be able to use it more than once.
 * To avoid this, use fillAndWrite() which does not mutate the form.
 */
public interface Form extends CanClone<Form>, Mutable {

    static @NotNull Form empty() {
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
    @NotNull Form leaveBlank(BlankSpace blankSpace);

    @Contract("_, _->this")
    @NotNull Form fillOut(BlankSpace.SingleValue blankSpace, String value);

    @Contract("_, _->this")
    @NotNull Form fillOut(BlankSpace.MultiValue blankSpace, Iterable<String> values);

    @Contract("_, _->this")
    default @NotNull Form fillOut(BlankSpace.MultiValue blankSpace, String... values) {
        fillOut(blankSpace, Sequential.of(values));
        return this;
    }

    @Contract("_->this")
    @NotNull Form fillOut(Map.MultiValue<BlankSpace, String> map);

    @NotNull Sequential<Or<String, BlankSpace>> getSequence();

    interface BlankSpace {

        boolean canBeLeftBlank();

        @NotNull String leaveBlank();

        interface MultiValue extends BlankSpace {
            @NotNull String enterValues(Iterable<String> values);

            @Override
            default @NotNull String leaveBlank() {
                return enterValues(Collections.emptySet());
            }

            @FunctionalInterface
            interface CanBeLeftBlank extends MultiValue {
                @Override
                default boolean canBeLeftBlank() {
                    return true;
                }
            }

            @FunctionalInterface
            interface CanNotBeLeftBlank extends MultiValue {
                @Override
                default boolean canBeLeftBlank() {
                    return true;
                }
            }
        }

        interface SingleValue extends BlankSpace {
            @NotNull String enterValue(@Nullable String value);

            @Override
            default @NotNull String leaveBlank() {
                return enterValue(null);
            }

            @FunctionalInterface
            interface CanBeLeftBlank extends SingleValue {
                @Override
                default boolean canBeLeftBlank() {
                    return true;
                }
            }

            @FunctionalInterface
            interface CanNotBeLeftBlank extends SingleValue {
                @Override
                default boolean canBeLeftBlank() {
                    return true;
                }
            }
        }
    }
}
