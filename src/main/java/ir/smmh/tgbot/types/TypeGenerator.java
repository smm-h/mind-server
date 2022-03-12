package ir.smmh.tgbot.types;

import ir.smmh.meta.*;
import ir.smmh.meta.impl.*;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.Form.IncompleteFormException;
import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.StringUtil;
import ir.smmh.util.jile.Or;
import ir.smmh.util.jile.impl.SlimOr;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;

class TypeGenerator {

    // TODO handle "returned only with" attributes manually

    private static final String
            PREFIX_ARRAY = "Array of ",
            PREFIX_ARRAY_2D = "Array of Array of ";

    public static void main(String[] args) throws IOException {
        Document d = Jsoup.parse(new URL("https://core.telegram.org/bots/api"), 0);
        JavaPackage pkg = JavaPackage.of("ir.smmh.tgbot.types.gen");
        JavaPackage impl = pkg.subpackage("impl");
        Sequential<String> commonImports = Sequential.ofArguments(
                pkg.star(),
                JSONUtil.class.getCanonicalName(),
                Sequential.class.getCanonicalName(),
                Contract.class.getCanonicalName(),
                NotNull.class.getCanonicalName(),
                Nullable.class.getCanonicalName(),
                JSONObject.class.getCanonicalName());
        Sequential<Argument> argumentsForOf
                = Sequential.ofArguments(new ArgumentImpl(
                new TypeSpecifierImpl(true, "JSONObject"), "wrapped"));
        Sequential<Argument> argumentsForConstructor
                = Sequential.ofArguments(new ArgumentImpl(
                new TypeSpecifierImpl(false, "JSONObject"), "wrapped"));
        Sequential<Argument> argumentsForOfGeneral
                = Sequential.ofArguments(new ArgumentImpl(
                new TypeSpecifierImpl(true, "Object"), "wrapped"));
        for (Element header : d.getElementsByTag("h3")) {
            if (header.text().equals("Available types")) {
                Element element = header;
                JavaInterface abstractType = null;
                JavaClass concreteType = null;
                boolean inType = false;
                while (true) {
                    element = element.nextElementSibling();
                    if (element == null) break;
                    String tagName = element.tagName();
                    String text = element.text();
                    if (tagName.equals("h4")) {
                        if (inType) {
                            try {
                                abstractType.generateToFile(false);
                                concreteType.generateToFile(false);
                            } catch (IncompleteFormException e) {
                                System.err.println(e.getMessage() + " in " + abstractType.getTypeName());
                            }
                        }
                        if (StringUtil.contains(text, ' ')) {
                            inType = false;
                        } else if (Character.isLowerCase(text.charAt(0))) {
                            // TODO inMethod
                            inType = false;
                        } else {
                            inType = true;

                            // create an interface and a class for the type
                            abstractType = new JavaInterfaceImpl(pkg, text);
                            concreteType = new JavaClassImpl(impl, abstractType);
                            abstractType.addImport(concreteType);

                            // import some stuff in both
                            abstractType.enter(JavaType.IMPORTS, commonImports);
                            concreteType.enter(JavaType.IMPORTS, commonImports);

                            // define their super-types
                            abstractType.enter(JavaInterface.SUPER_INTERFACES, "JSONUtil.ReadOnlyJSON");
                            concreteType.enter(JavaClass.SUPER_CLASS, "JSONUtil.ReadOnlyJSONImpl");

                            // add constructors
                            TypeSpecifier rt = abstractType.getSpecifier(null);
                            Method ofGeneral = new StaticMethod(abstractType, null, rt, "of", argumentsForOfGeneral, "return of((JSONObject) wrapped);");
                            ofGeneral.enter(Method.ANNOTATIONS, "Contract(\"!null->!null\")");
                            ofGeneral.addToType();
                            Method ofAbstract = new StaticMethod(abstractType, null, rt, "of", argumentsForOf, "return " + concreteType.getTypeName() + ".of(wrapped);");
                            ofAbstract.enter(Method.ANNOTATIONS, "Contract(\"!null->!null\")");
                            ofAbstract.addToType();

                            new Constructor(concreteType, null, argumentsForConstructor, "super(wrapped);").addToType();
                            Method ofConcrete = new StaticMethod(concreteType, null, rt, "of", argumentsForOf, "return wrapped == null ? null : new " + concreteType.getTypeName() + "(wrapped);");
                            ofConcrete.enter(Method.ANNOTATIONS, "Contract(\"!null->!null\")");
                            ofConcrete.addToType();
                        }
                    } else {
                        if (inType) {
                            switch (tagName) {
                                case "h3":
//                                    System.out.println("H3: " + text);
                                    break;
                                case "p":
                                case "blockquote":
                                    abstractType.addDocumentation(text);
                                    concreteType.addDocumentation(text);
                                    break;
                                case "ul":
                                    // TODO automate inheritance via unordered list
//                                    System.out.println("UL: " + text);
                                    break;
                                case "table":
                                    for (Element row : element.child(1).children()) {
                                        String name = row.child(0).text();
                                        String returnType = row.child(1).text();
                                        String doc = row.child(2).text().replaceAll("[\u201C\u201D]", "\"");
                                        if (doc.contains("Optional") && !doc.startsWith("Optional"))
                                            System.err.println("Weird method doc: " + abstractType.getTypeName() + "#" + name);
                                        boolean isNullable = doc.startsWith("Optional");
                                        boolean useLongInsteadOfInt = doc.contains("silent defects");
                                        final String getter;
                                        boolean isPrimitive = false;
                                        if (returnType.startsWith(PREFIX_ARRAY_2D)) {
                                            String typeName = StringUtil.removePrefix(returnType, PREFIX_ARRAY_2D);
                                            if (useLongInsteadOfInt && typeName.equals("Integer")) typeName = "Long";
                                            returnType = "Sequential<Sequential<" + typeName + ">>";
                                            final String convertor;
                                            if (canBeCast(typeName)) {
                                                convertor = "FunctionalUtil::cast";
                                                concreteType.addImport(FunctionalUtil.class);
                                            } else {
                                                convertor = typeName + "::of";
                                            }
                                            getter = "get" + (isNullable ? "Nullable" : "") + "2DSequential(\"" + name + "\", " + convertor + ")";
                                        } else if (returnType.startsWith(PREFIX_ARRAY)) {
                                            String typeName = StringUtil.removePrefix(returnType, PREFIX_ARRAY);
                                            if (useLongInsteadOfInt && typeName.equals("Integer")) typeName = "Long";
                                            returnType = "Sequential<" + typeName + ">";
                                            final String convertor;
                                            if (canBeCast(typeName)) {
                                                convertor = "FunctionalUtil::cast";
                                                concreteType.addImport(FunctionalUtil.class);
                                            } else {
                                                convertor = typeName + "::of";
                                            }
                                            getter = "get" + (isNullable ? "Nullable" : "") + "Sequential(\"" + name + "\", " + convertor + ")";
                                        } else if (returnType.contains(" or ")) {
                                            String[] orTypes = returnType.split(" or ");
                                            if (orTypes.length == 2) {
                                                String thisType = orTypes[0];
                                                String thatType = orTypes[1];
                                                if (useLongInsteadOfInt && thisType.equals("Integer"))
                                                    thisType = "Long";
                                                if (useLongInsteadOfInt && thatType.equals("Integer"))
                                                    thatType = "Long";
                                                returnType = "Or<" + thisType + ", " + thatType + ">";
                                                abstractType.addImport(Or.class);
                                                concreteType.addImport(Or.class);
                                                concreteType.addImport(SlimOr.class);
                                                getter = "SlimOr.either(" + makeGetter(thisType, name, true) + ", " + makeGetter(thatType, name, true) + ")";
                                            } else {
                                                // TODO or with more than two types
                                                throw new UnsupportedOperationException("Or with more than two types " + returnType + " in " + abstractType.getTypeName());
                                            }
                                        } else {
                                            if (useLongInsteadOfInt && returnType.equals("Integer"))
                                                returnType = "Long";
                                            if (returnType.equals("Float number"))
                                                returnType = "Float";
                                            if (returnType.equals("True"))
                                                isNullable = false;
                                            getter = makeGetter(returnType, name, isNullable);
                                            if (!isNullable) {
                                                @Nullable String primitiveReturnType = primitiveOf(returnType);
                                                if (primitiveReturnType != null) {
                                                    returnType = primitiveReturnType;
                                                    isPrimitive = true;
                                                }
                                            }
                                        }
                                        TypeSpecifier rt = new TypeSpecifierImpl(isNullable, returnType, isPrimitive);
                                        OverrideableMethod om = new OverrideableMethod(abstractType, doc, rt, name, Sequential.empty());
                                        om.addToType();
                                        new OverridingMethod(concreteType, om, "return " + getter + ";").addToType();
                                    }
                                    break;
                                case "div":
                                case "hr":
                                    break;
                                default:
                                    System.err.println("Unused tag: " + tagName);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static @Nullable String primitiveOf(String type) {
        switch (type) {
            case "True":
            case "Boolean":
                return "boolean";
            case "Integer":
                return "int";
            case "Float":
                return "float";
            case "Long":
                return "long";
            default:
                return null;
        }
    }

    private static String makeGetter(String type, String name, boolean isNullable) {
        String arg = "(\"" + name + "\")";
        String get = isNullable ? "getNullable" : "get";
        switch (type) {
            case "True":
                return "has" + arg;
            case "Boolean":
            case "Integer":
            case "Float":
            case "Long":
            case "String":
                return get + type + arg;
            default:
                return type + ".of(" + get + "JSONObject" + arg + ")";
        }
    }

    private static boolean canBeCast(String type) {
        switch (type) {
            case "Boolean":
            case "Integer":
            case "Float":
            case "Long":
            case "String":
                return true;
            default:
                return false;
        }
    }
}
