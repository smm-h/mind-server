package annotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.StringJoiner;

@SupportedAnnotationTypes("annotations.Singleton")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class SingletonProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "BRUH MOMENT");
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("HELLO");
        Path path = Path.of("D:/test.txt");
        for (TypeElement te : annotations) {
            te.getQualifiedName().toString();
        }
        try {
            Files.writeString(path, joiner.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
