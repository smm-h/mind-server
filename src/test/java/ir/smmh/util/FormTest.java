package ir.smmh.util;

import static ir.smmh.meta.JavaFile.*;

class FormTest {
    public static void main(String[] args) {
        System.out.println(emptyJavaFile("nice")
                .enter(PACKAGE, "ir.smmh.nice")
                .enter(IMPORTS, "org.jetbrains.annotations.NotNull", "org.jetbrains.annotations.Nullable")
                .enter(DOC, "Nice.")
        );
    }
}