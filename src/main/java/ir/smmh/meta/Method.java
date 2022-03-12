package ir.smmh.meta;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.Form;
import ir.smmh.util.StringUtil;
import ir.smmh.util.impl.MultiValueBlankSpace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Method extends Form {

    BlankSpace ANNOTATIONS = new MultiValueBlankSpace("annotations", "@", "\n", "");
    BlankSpace QUALIFIERS = new MultiValueBlankSpace("qualifier", "", " ", "");
    BlankSpace RETURN_TYPE = new BlankSpace.ZeroOrOne("return type") {
        @Override
        public @NotNull String compose(Sequential<String> values) {
            return values.isEmpty() ? "" : values.getSingleton() + " ";
        }
    };
    BlankSpace NAME = BlankSpace.itself("method name");
    BlankSpace ARGUMENTS = new MultiValueBlankSpace("arguments", "", ", ", "", "(", ")", "()");
    BlankSpace BODY = new BlankSpace.ZeroOrOne("method body") {
        @Override
        public @NotNull String compose(Sequential<String> values) {
            return values.isEmpty() ? ";" : " {\n" + StringUtil.shiftRight(values.getSingleton(), 4) + "\n}";
        }
    };

    void addToType();

    @NotNull JavaType getType();

    @Nullable String getDocumentation();

    @NotNull TypeSpecifier getReturnType();

    @NotNull String getName();

    @NotNull Sequential<Argument> getArguments();

    @Nullable String getBody();

    boolean isStatic();
}
