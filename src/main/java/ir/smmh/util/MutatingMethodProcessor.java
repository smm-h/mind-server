package ir.smmh.util;

import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.Trees;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_11) // adapt when using older version
@SupportedAnnotationTypes(MutatingMethodProcessor.ANNOTATION_NAME)
public class MutatingMethodProcessor extends AbstractProcessor {

    static final String ANNOTATION_NAME = "ir.smmh.util.MutatingMethod";
    static final String REQUIRED_FIRST = "preMutate", REQUIRED_LAST = "postMutate";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Optional<? extends TypeElement> o = annotations.stream()
                .filter(e -> ANNOTATION_NAME.contentEquals(e.getQualifiedName())).findAny();
        if (o.isEmpty()) return false;
        TypeElement myAnnotation = o.get();

        roundEnv.getElementsAnnotatedWith(myAnnotation).forEach(this::check);

        return true;
    }

    // the inherited method does already store the processingEnv
    // public void init(ProcessingEnvironment processingEnv) {

    private void check(Element e) {
        Trees trees = Trees.instance(processingEnv);
        Tree tree = trees.getTree(e);
        if (tree.getKind() != Kind.METHOD) { // should not happen as compiler handles @Target
            processingEnv.getMessager()
                    .printMessage(Diagnostic.Kind.ERROR, ANNOTATION_NAME + " only allowed at methods", e);
            return;
        }
        MethodTree m = (MethodTree) tree;
        List<? extends StatementTree> statements = m.getBody().getStatements();
        if (statements.isEmpty() || !isRequiredFirst(statements.get(0))) {
            processingEnv.getMessager()
                    .printMessage(Diagnostic.Kind.MANDATORY_WARNING,
                            "Mutating method does not start with " + REQUIRED_FIRST + "();", e);
        }
        // open challenges:
        //   - accept a return statement after postMutate();
        //   - allow a try { body } finally { postMutate(); }
        if (statements.isEmpty() || !isRequiredLast(statements.get(statements.size() - 1))) {
            processingEnv.getMessager()
                    .printMessage(Diagnostic.Kind.MANDATORY_WARNING,
                            "Mutating method does not end with " + REQUIRED_LAST + "();", e);
        }
    }

    private boolean isRequiredFirst(StatementTree st) {
        return invokes(st, REQUIRED_FIRST);
    }

    private boolean isRequiredLast(StatementTree st) {
        return invokes(st, REQUIRED_LAST);
    }

    // check whether tree is an invocation of a no-arg method of the given name
    private boolean invokes(Tree tree, String method) {
        if (tree.getKind() != Kind.EXPRESSION_STATEMENT) return false;
        tree = ((ExpressionStatementTree) tree).getExpression();
        if (tree.getKind() != Kind.METHOD_INVOCATION) return false;

        MethodInvocationTree i = (MethodInvocationTree) tree;

        if (!i.getArguments().isEmpty()) return false; // not a no-arg method

        ExpressionTree ms = i.getMethodSelect();
        // TODO add support for explicit this.method()
        return ms.getKind() == Kind.IDENTIFIER
                && method.contentEquals(((IdentifierTree) ms).getName());
    }

}