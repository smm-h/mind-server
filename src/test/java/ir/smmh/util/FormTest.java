package ir.smmh.util;

import ir.smmh.util.impl.MultiValueFormBlankSpace;
import ir.smmh.util.impl.SingleValueFormBlankSpace;

class FormTest {

    private final static Form.BlankSpace.SingleValue PACKAGE
            = new SingleValueFormBlankSpace("package ", ";\n");
    private final static Form.BlankSpace.MultiValue IMPORTS
            = new MultiValueFormBlankSpace("import ", ";\n", "\n", "", "");
    private final static Form.BlankSpace.SingleValue DOC = (Form.BlankSpace.SingleValue.CanBeLeftBlank) value -> {
        if (value == null) return "";
        int limit = 80;
        int n = value.length();
        StringBuilder b = new StringBuilder(n + (StringUtil.count(value, '\n') + n / limit + 8) * 4);
        b.append("\n/**\n");
        for (String s : StringUtil.splitByCharacter(value, '\n')) {
            for (String line : StringUtil.splitByLength(s, limit - 3)) {
                b.append(" * ").append(line);
            }
        }
        b.append("\n */\n");
        return b.toString();
    };

    public static void main(String[] args) {
        System.out.println(Form.empty()
                        .append(PACKAGE)
                        .append(IMPORTS)
                        .append(DOC)
                        .fillOut(PACKAGE, "ir.smmh.nice")
                        .leaveBlank(IMPORTS)
//                .fillOut(IMPORTS, "org.jetbrains.annotations.NotNull", "org.jetbrains.annotations.Nullable")
                        .fillOut(DOC, "Nice.")
        );
    }
}