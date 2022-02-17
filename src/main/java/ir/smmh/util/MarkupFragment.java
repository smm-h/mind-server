package ir.smmh.util;

import java.util.Objects;

public class MarkupFragment {
    private final String data;

    public MarkupFragment(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof MarkupFragment && Objects.equals(((MarkupFragment) o).data, data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }
}
