package ir.smmh.meta;

import ir.smmh.meta.impl.TypeSpecifierImpl;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.Form;
import ir.smmh.util.StringUtil;
import ir.smmh.util.impl.MultiValueBlankSpace;
import ir.smmh.util.impl.SingleValueBlankSpace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.StringJoiner;

public interface JavaType extends Form {

    BlankSpace NAME = BlankSpace.itself("type name");
    BlankSpace PACKAGE = new SingleValueBlankSpace("package address", "package ", ";\n");
    BlankSpace IMPORTS = new MultiValueBlankSpace("list of imports", "import ", "\n", ";", "\n", "\n", "");
    BlankSpace JAVADOC = new BlankSpace.ZeroOrMore("documentation") {
        @Override
        public @NotNull String compose(@NotNull Sequential<String> values) {
            int n = values.getSize();
            if (n == 0) return "";
            String value = values.getSingleton();
            int limit = 80;
            int capacity = n + (StringUtil.count(value, '\n') + n / limit + 8) * 4;
            StringBuilder builder = new StringBuilder(capacity);
            builder.append("\n/**\n");
            boolean firstTime = true;
            for (String s : StringUtil.splitByCharacter(value, '\n')) {
                for (String line : StringUtil.splitByLength(s, limit - 3)) {
                    builder.append(" * ").append(line).append('\n');
                }
                if (firstTime)
                    firstTime = false;
                else
                    builder.append("\n");
            }
            builder.append(" */\n");
//            System.out.println("CAPACITY ESTIMATION RESULTS: ACTUAL=" + b.length() + " ESTIMATED=" + capacity);
            return builder.toString();
        }
    };
    BlankSpace METHODS = new BlankSpace.ZeroOrMore("methods") {
        @Override
        public @NotNull String compose(@NotNull Sequential<String> values) {
            StringJoiner joiner = new StringJoiner("\n\n");
            for (String value : values) joiner.add(value);
            return "{\n" + StringUtil.shiftRight(joiner.toString(), 4) + "\n}";
        }
    };

    default TypeSpecifier getSpecifier(@Nullable Boolean isNullable) {
        return new TypeSpecifierImpl(isNullable, getTypeName());
    }

    @NotNull String getTypeName();

    @NotNull JavaPackage getPackage();

    void generateToFile() throws IOException;

    void addDocumentation(String text);

    default @NotNull String getFullyQualifiedName() {
        return getPackage().getAddress() + "." + getTypeName();
    }

    void addMethod(Method method);
}
