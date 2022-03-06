package ir.smmh.util;

import static ir.smmh.meta.JavaFile.*;

class FormTest {
    public static void main(String[] args) {
        System.out.println(blank()
                .fillOut(PACKAGE, "ir.smmh.nice")
                .leaveBlank(IMPORTS)
                .fillOut(IMPORTS, "org.jetbrains.annotations.NotNull", "org.jetbrains.annotations.Nullable")
                .fillOut(DOC, "Nice.")
        );
    }
}