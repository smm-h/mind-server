package ir.smmh.meta;

import ir.smmh.meta.impl.TypeSpecifierImpl;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
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
    BlankSpace IMPORTS = new MultiValueBlankSpace("list of imports", "import ", ";\n", "\n", "\n", "");
    BlankSpace JAVADOC = new BlankSpace.ZeroOrMore("documentation") {
        @Override
        public @NotNull String compose(@NotNull Sequential<String> values) {
            if (values.isEmpty()) return "";
            int indent = 1;
            int softLimit = 80 - indent * 4;
            int hardLimit = 120 - indent * 4;
            Sequential.Mutable.VariableSize<String> paragraphs = new SequentialImpl<>();
            int totalLength = 0;
            int estimatedLineBreaks = 0;
            for (String value : values) {
                for (String paragraph : StringUtil.splitByCharacter(value, '\n')) {
                    String trimmedParagraph = paragraph.trim();
                    if (!trimmedParagraph.isEmpty()) {
                        paragraphs.append(trimmedParagraph);
                        int trimmedParagraphLength = trimmedParagraph.length();
                        totalLength += trimmedParagraphLength;
                        estimatedLineBreaks += Math.ceil(trimmedParagraphLength / (float) softLimit);
                    }
                }
            }
            int estimatedCapacity = totalLength + (estimatedLineBreaks + 1) * 4 + Math.max(0, paragraphs.getSize() - 1) * 7 + 8;
            StringBuilder builder = new StringBuilder(estimatedCapacity);
            builder.append("/**\n"); // 4
            boolean firstTime = true;
            for (String paragraph : paragraphs) {
                if (firstTime)
                    firstTime = false;
                else
                    builder.append(" * <p>\n"); // 7*(P-1)
                int i = 0; // overall index
                int x = 0; // in-line index
                int n = paragraph.length();
                while (i < n) {
                    builder.append(" * "); // (3+1)*(LB+1)
                    while (true) {
                        if (i == n)
                            break;
                        char c = paragraph.charAt(i++);
                        x++;
                        if (x > softLimit && c == 32)
                            break;
                        builder.append(c);
                        if (x > hardLimit)
                            break;
                    }
                    builder.append("\n");
                    x = 0;
                }
            }
            builder.append(" */\n"); // 4
            int actualSize = builder.length();
            if (actualSize - estimatedCapacity == 5)
                System.out.println("CAPACITY ESTIMATION RESULTS: " + (actualSize - estimatedCapacity));
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

    void generateToFile(boolean overwrite) throws IOException;

    void addDocumentation(String text);

    default @NotNull String getFullyQualifiedName() {
        return getPackage().getAddress() + "." + getTypeName();
    }

    void addMethod(Method method);

    default void addImport(Class<?> c) {
        addImport(c.getCanonicalName());
    }

    default void addImport(JavaType type) {
        addImport(type.getFullyQualifiedName());
    }

    default void addImport(JavaPackage pkg) {
        addImport(pkg.star());
    }

    void addImport(String canonicalName);
}
