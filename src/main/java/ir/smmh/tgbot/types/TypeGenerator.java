package ir.smmh.tgbot.types;

import ir.smmh.meta.*;
import ir.smmh.meta.impl.*;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.util.Form.IncompleteFormException;
import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.Map;
import ir.smmh.util.StringUtil;
import ir.smmh.util.impl.MapImpl;
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

    private static final String
            PREFIX_ARRAY = "Array of ",
            PREFIX_ARRAY_2D = "Array of Array of ";

    public static void main(String[] args) throws IOException {
        Document document = Jsoup.parse(new URL("https://core.telegram.org/bots/api"), 0);
        JavaPackage pkg = JavaPackage.of("ir.smmh.tgbot.types.gen");
        JavaPackage impl = pkg.subpackage("impl");
        Sequential<Object> commonImports = Sequential.ofArguments(
                JSONUtil.class,
                Contract.class,
                NotNull.class,
                Nullable.class,
                JSONObject.class);
        Sequential<Argument> preMadeArgumentsForOf
                = Sequential.ofArguments(new ArgumentImpl(
                new TypeSpecifierImpl(true, "JSONObject"), "wrapped"));
        Sequential<Argument> preMadeArgumentsForConstructor
                = Sequential.ofArguments(new ArgumentImpl(
                new TypeSpecifierImpl(false, "JSONObject"), "wrapped"));
        Sequential<Argument> preMadeArgumentsForOfGeneral
                = Sequential.ofArguments(new ArgumentImpl(
                new TypeSpecifierImpl(true, "Object"), "wrapped"));
        Map.MultiValue.Mutable<String, String> generalTypes = new MapImpl.MultiValue.Mutable<>();
        for (Element header : document.getElementsByTag("h3")) {
            if (header.text().equals("Getting updates")) {
                Element element = header;
                JavaInterface abstractType = null;
                JavaClass concreteType = null;
                Sequential.Mutable.VariableSize<Argument> constructorArguments
                        = new SequentialImpl<>();
                StringBuilder constructorBodyBuilder
                        = new StringBuilder();
                StringBuilder constructorDocumentationBuilder
                        = new StringBuilder();
                Mode mode = Mode.NONE;
                while (true) {
                    element = element.nextElementSibling();
                    if (element == null) break;
                    String tagName = element.tagName();
                    String text = element.text();
                    if (tagName.equals("h4") || tagName.equals("h3")) {
                        if (mode == Mode.IN_TYPE) {
                            try {
                                constructorBodyBuilder.append(");");
                                new Constructor(concreteType, constructorDocumentationBuilder.toString(), constructorArguments, constructorBodyBuilder.toString()).addToType();
                                abstractType.generateToFile(false);
                                concreteType.generateToFile(false);
                            } catch (IncompleteFormException e) {
                                System.err.println(e.getMessage() + " in " + abstractType.getTypeName());
                            }
                        }
                        mode = Mode.NONE;
                    }
                    if (tagName.equals("h4")) {
                        if (!StringUtil.contains(text, ' ')) {
                            //noinspection StatementWithEmptyBody
                            if (Character.isLowerCase(text.charAt(0))) {
//                                mode = Mode.IN_METHOD TODO
                            } else {
                                mode = Mode.IN_TYPE;

                                // create an interface and a class for the type
                                abstractType = new JavaInterfaceImpl(pkg, text);
                                concreteType = new JavaClassImpl(impl, abstractType);
                                abstractType.addImport(concreteType);

                                // import some stuff in both
                                abstractType.addImports(commonImports);
                                concreteType.addImports(commonImports);

                                // import all abstract types in each concrete type
                                concreteType.addImport(pkg);

                                // define their super-types
                                String myGeneralType = generalTypes.containingKey(text);
                                if (myGeneralType == null) {
                                    abstractType.enter(JavaInterface.SUPER_INTERFACES, "JSONUtil.ReadOnlyJSON");
                                } else {
                                    generalTypes.removeAtPlace(myGeneralType, text);
                                    abstractType.enter(JavaInterface.SUPER_INTERFACES, myGeneralType);
                                }
                                concreteType.enter(JavaClass.SUPER_CLASS, "JSONUtil.ReadOnlyJSONImpl");

                                // add constructors
                                TypeSpecifier rt = abstractType.getSpecifier(null);
                                Method ofGeneral = new StaticMethod(abstractType, null, rt, "of", preMadeArgumentsForOfGeneral, "return of((JSONObject) wrapped);");
                                ofGeneral.enter(Method.ANNOTATIONS, "Contract(\"!null->!null\")");
                                ofGeneral.addToType();
                                Method ofAbstract = new StaticMethod(abstractType, null, rt, "of", preMadeArgumentsForOf, "return " + concreteType.getTypeName() + ".of(wrapped);");
                                ofAbstract.enter(Method.ANNOTATIONS, "Contract(\"!null->!null\")");
                                ofAbstract.addToType();

                                new Constructor(concreteType, null, preMadeArgumentsForConstructor, "super(wrapped);").addToType();
                                Method ofConcrete = new StaticMethod(concreteType, null, rt, "of", preMadeArgumentsForOf, "return wrapped == null ? null : new " + concreteType.getTypeName() + "(wrapped);");
                                ofConcrete.enter(Method.ANNOTATIONS, "Contract(\"!null->!null\")");
                                ofConcrete.addToType();

                                // add JSON-making constructor
                                constructorArguments.clear();
                                constructorBodyBuilder.setLength(0);
                                constructorDocumentationBuilder.setLength(0);
                                constructorBodyBuilder.append("this(new JSONObject()");
                            }
                        }
                    } else {
                        if (mode == Mode.IN_TYPE) {
                            switch (tagName) {
                                case "p":
                                case "blockquote":
                                    abstractType.addDocumentation(text);
                                    concreteType.addDocumentation(text);
                                    break;
                                case "ul":
                                    String generalType = abstractType.getTypeName();
                                    for (Element item : element.children())
                                        generalTypes.addAtPlace(generalType, item.text());
                                    break;
                                case "table":
                                    for (Element row : element.child(1).children()) {
                                        String name = row.child(0).text();
                                        String returnType = row.child(1).text();
                                        String doc = row.child(2).text().replaceAll("[\u201C\u201D]", "\"");
                                        boolean isNullable = doc.startsWith("Optional");
                                        boolean useLongInsteadOfInt = doc.contains("silent defects");
                                        final String getter;
                                        boolean isPrimitive = false;
                                        if (returnType.startsWith(PREFIX_ARRAY_2D)) {
                                            abstractType.addImport(Sequential.class);
                                            concreteType.addImport(Sequential.class);
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
                                            abstractType.addImport(Sequential.class);
                                            concreteType.addImport(Sequential.class);
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
                                        TypeSpecifier typeSpecifier = new TypeSpecifierImpl(isNullable, returnType, isPrimitive);
                                        // TODO remove constructor arguments and getters of fixed-value attributes
                                        // = non-null string attribute of a type, denoted by either suffix `, must be $`
                                        // or suffix `always "$"` of their doc text. Does not work in methods.
                                        constructorBodyBuilder
                                                .append("\n        .put(\"")
                                                .append(name)
                                                .append("\", ")
                                                .append(name)
                                                .append(")");
                                        constructorDocumentationBuilder
                                                .append("\n@param ")
                                                .append(name)
                                                .append(' ')
                                                .append(doc);
                                        constructorArguments
                                                .append(new ArgumentImpl(typeSpecifier, name));
                                        OverrideableMethod om = new OverrideableMethod(abstractType, doc, typeSpecifier, name, Sequential.empty());
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

    private enum Mode {
        NONE, IN_TYPE, IN_METHOD
    }
}
