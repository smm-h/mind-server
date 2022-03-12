package ir.smmh.util;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.DoubleSequence;
import ir.smmh.nile.adj.impl.SingleSequence;
import ir.smmh.nile.verbs.CanClone;
import ir.smmh.util.jile.Or;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

/**
 * A form is a plain text document with blank spaces to fill out or leave blank.
 * It is used for generating strings from patterns. Some spaces may accept more
 * than one value. Start with StringForm.empty() and chain methods to build your
 * form, and finish with generate(). The filling out methods do not mutate the
 * form, meaning you can use it more than once.
 */
public interface Form extends Mutable, CanClone<Form> {
    @NotNull Form copy(String title);

    /**
     * This method helps you use a pre-made string form, fill it, and write its
     * contents to a file.
     *
     * @throws IncompleteFormException If filling out the form fails
     * @throws IOException             If writing to file fails
     */
    void generateToFile(Path destination, boolean overwrite) throws IOException;

    /**
     * @throws IncompleteFormException If filling out the form fails
     */
    @NotNull String generate();

    @NotNull String getTitle();

    @Contract("_, _->this")
    @NotNull Form enter(BlankSpace blankSpace, @Nullable String entry);

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

    @Contract("_, _->this")
    @NotNull Form enter(BlankSpace blankSpace, Sequential<String> entries);

    @Contract("_, _->this")
    default @NotNull Form enter(BlankSpace blankSpace, String... entries) {
        enter(blankSpace, Sequential.of(entries));
        return this;
    }

    @Contract("_->this")
    @NotNull Form enter(Map.MultiValue<BlankSpace, String> mappedEntries);

    @NotNull Sequential<Or<String, BlankSpace>> getSequence();

    interface BlankSpace {
        static BlankSpace itself(String title) {
            return new Form.BlankSpace.ExactlyOne(title) {
                @Override
                public @NotNull String compose(@NotNull Sequential<String> values) {
                    return values.getSingleton();
                }
            };
        }

        @NotNull String getTitle();

        default boolean acceptsCount(int count) {
            int min = getMinimumCount();
            int max = getMaximumCount();
            return count >= min && (max == -1 || count <= max);
        }

        static String countToString(int count) {
            return count == 1 ? "1 entry" : count + " entries";
        }

        default String countErrorMessage(int count) {
            int min = getMinimumCount();
            int max = getMaximumCount();
            StringBuilder builder = new StringBuilder();
            builder
                    .append("needs ");
            if (min == max) {
                builder
                        .append("exactly ")
                        .append(countToString(min));
            } else {
                builder
                        .append("at least ")
                        .append(countToString(min));
                if (max != -1) builder
                        .append(", and at most ")
                        .append(countToString(max));
            }
            builder
                    .append(", got ")
                    .append(countToString(count))
                    .append(" instead");
            return builder.toString();
        }

        int getMinimumCount();

        int getMaximumCount();

        @NotNull String compose(Sequential<String> values);

        default @NotNull String compose() {
            return compose(Sequential.empty());
        }

        default @NotNull String compose(String singleValue) {
            return compose(new SingleSequence<>(singleValue));
        }

        default @NotNull String compose(String value1, String value2) {
            return compose(new DoubleSequence<>(value1, value2));
        }

        abstract class ZeroOrMore extends BlankSpace.Impl {
            public ZeroOrMore(String title) {
                super(title, 0, -1);
            }
        }

        abstract class OneOrMore extends BlankSpace.Impl {
            public OneOrMore(String title) {
                super(title, 1, -1);
            }
        }

        abstract class ZeroOrOne extends BlankSpace.Impl {
            public ZeroOrOne(String title) {
                super(title, 0, 1);
            }
        }

        abstract class ExactlyOne extends BlankSpace.Impl {
            public ExactlyOne(String title) {
                super(title, 1, 1);
            }
        }

        abstract class Impl implements BlankSpace {

            private final String title;
            private final int minimumCount, maximumCount;

            public Impl(String title, int minimumCount, int maximumCount) {
                if (minimumCount < 0 || (maximumCount != -1 && (minimumCount > maximumCount || maximumCount <= 0)))
                    throw new IllegalArgumentException("invalid range");
                this.title = title;
                this.minimumCount = minimumCount;
                this.maximumCount = maximumCount;
            }

            @Override
            public @NotNull String getTitle() {
                return title;
            }

            @Override
            public int getMinimumCount() {
                return minimumCount;
            }

            @Override
            public int getMaximumCount() {
                return maximumCount;
            }
        }
    }

    class IncompleteFormException extends RuntimeException {
        private final BlankSpace blankSpace;

        public IncompleteFormException() {
            this(null, null);
        }

        public IncompleteFormException(BlankSpace blankSpace) {
            this(blankSpace, null);
        }

        public IncompleteFormException(String message) {
            this(null, message);
        }

        public IncompleteFormException(@Nullable BlankSpace blankSpace, @Nullable String extraMessage) {
            super(makeMessage(blankSpace, extraMessage));
            this.blankSpace = blankSpace;
        }

        private static String makeMessage(@Nullable BlankSpace blankSpace, @Nullable String extraMessage) {
            String message = "Missing blankspace";
            if (blankSpace != null)
                message += ": " + blankSpace.getTitle().toUpperCase(Locale.ROOT);
            if (extraMessage != null)
                message += ", " + extraMessage;
            return message;
        }

        public @Nullable BlankSpace getBlankSpace() {
            return blankSpace;
        }
    }
}
