package ir.smmh.lingu.json;

import ir.smmh.lingu.Encodeable;
import ir.smmh.lingu.impl.CodeImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class JSON {
    private JSON() {
    }

    public interface Element extends Encodeable {
        java.lang.String serialize();

        @Override
        default CodeImpl encode() {
            return new CodeImpl(serialize(), "json");
        }
    }

    public static class Map extends HashMap<String, Element> implements Element {
        @Override
        public java.lang.String serialize() {
            if (size() == 0)
                return "{}";
            else {
                StringBuilder builder = new StringBuilder(14 * size());
                builder.append('{');
                for (String key : keySet()) {
                    builder.append(key.serialize());
                    builder.append(": ");
                    builder.append(Objects.requireNonNull(get(key)).serialize());
                    builder.append(", ");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.deleteCharAt(builder.length() - 1);
                builder.append('}');
                return builder.toString();
            }
        }
    }

    public static class List extends LinkedList<Element> implements Element {
        @Override
        public java.lang.String serialize() {
            if (size() == 0)
                return "[]";
            else {
                StringBuilder builder = new StringBuilder(8 * size());
                builder.append('[');
                for (Element element : this) {
                    builder.append(element.serialize());
                    builder.append(", ");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.deleteCharAt(builder.length() - 1);
                builder.append(']');
                return builder.toString();
            }
        }
    }

    public static class String implements Element {
        public final String value;

        public String(String value) {
            this.value = value;
        }

        @Override
        public java.lang.String serialize() {
            return "\"" + value + "\"";
        }
    }

    public static class Number implements Element {
        public final double value;

        public Number(double value) {
            this.value = value;
        }

        @Override
        public java.lang.String serialize() {
            return Double.toString(value);
        }
    }

    public static class Boolean implements Element {
        public final boolean value;

        public Boolean(boolean value) {
            this.value = value;
        }

        @Override
        public java.lang.String serialize() {
            return value ? "true" : "false";
        }
    }
}
