package ir.smmh.mind;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface CommonIdeas {

    Idea NOTHING = new Idea() {
        @Override
        public String getName() {
            return "Nothing";
        }

        @Override
        public @Nullable Set<Idea> getDirectIntensions() {
            return null;
        }

        @Override
        public @Nullable Set<Property<?>> getDirectProperties() {
            return null;
        }

        @Override
        public @NotNull Instance instantiate() {
            return null;
        }
    };

    Idea ANYTHING = new Idea() {
        @Override
        public String getName() {
            return "Anything";
        }

        @Override
        public @Nullable Set<Idea> getDirectIntensions() {
            return null;
        }

        @Override
        public @Nullable Set<Property<?>> getDirectProperties() {
            return null;
        }

        @Override
        public @NotNull Instance instantiate() {
            return new Instance() {
                @Override
                public @NotNull Idea getType() {
                    return ANYTHING;
                }

                @Override
                public <T> boolean has(Property<T> property) {
                    return false;
                }

                @Override
                public <T> void set(Property<T> property, Object value) {

                }

                @Override
                public <T> @Nullable T get(Property<T> property) {
                    return null;
                }

                @Override
                public boolean is(Idea idea) {
                    return false;
                }

                @Override
                public void setLink(Idea idea, Instance instance) {

                }

                @Override
                public @Nullable Instance getLink(Idea idea) {
                    return null;
                }
            };
        }
    };
}
