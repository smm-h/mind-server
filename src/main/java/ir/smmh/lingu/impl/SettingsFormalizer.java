package ir.smmh.lingu.impl;

import ir.smmh.jile.common.Common;
import ir.smmh.jile.common.Range;
import ir.smmh.lingu.AbstractMishap;
import ir.smmh.lingu.CodeProcess;
import ir.smmh.lingu.IndividualTokenType.IndividualToken;
import ir.smmh.lingu.Language;
import ir.smmh.lingu.Token;
import ir.smmh.lingu.processors.Multiprocessor;
import ir.smmh.tree.jile.impl.LinkedTree;

import java.util.*;

public abstract class SettingsFormalizer extends Language {
    // implements Maker<Map<String, ? extends SettingsFormalizer.Settings>> {

    // Graph Out of A Tree = GOAT

    public abstract FormalizationBlueprint findType(String type);

    public abstract Settings wrap(FormalSettings src);

    public SettingsFormalizer(String name, String primaryExt) {
        this(name, primaryExt, primaryExt);
    }

    private final DefaultTokenizer tokenizer;

    public SettingsFormalizer(String name, String langPath, String primaryExt) {
        super(name, langPath, primaryExt, new Multiprocessor());

        tokenizer = TokenizerMaker.singleton().maker.makeFrom("settings-formalizer");
        processor.extend(tokenizer);

        setDefaultValues();
        setMainMaker(mapMaker);
    }

    /**
     * TODO
     * <p>
     * In case {@code parentForValues} is of a different type than this node, call
     * this method with a fallback instead, which is another {@code
     * AbsoluteSettings} whose name is the value of the key {@code 'like'} in any
     * node, and must have the same type as this node.
     *
     * <p>
     * If that name is not yet absolved the node enters a queue with that name, and
     * waits to be absolved. If the tree is traversed and that name is not absolved
     * either that name does not exist or there is a cycle which includes this node
     * and its fallback.
     */

    public FormalSettings absolve(InformalSettings subj, FormalSettings parentForName, FormalSettings parentForValues, CodeProcess process) {

        FormalizationBlueprint foundType = subj.type == null ? null : findType(subj.type.getData());

        // absolve name
        String absolvedName;

        if (subj.name == null) {
            absolvedName = "";

        } else {
            absolvedName = subj.name.getData();

            boolean postfix = absolvedName.charAt(0) == '-';
            boolean suffix = absolvedName.charAt(absolvedName.length() - 1) == '-';

            if (postfix && suffix) {
                process.issue(new BothPrefixAndSuffix(subj));
            } else if (postfix) {
                absolvedName = parentForName.name + absolvedName;
            } else if (suffix) {
                absolvedName += parentForName.name;
            }
        }

        if (foundType == null) {
            return new FormalSettings(subj, absolvedName, null, null, false);

        } else {
            // absolve values
            boolean complete = true;
            Token.Individual[][] absolvedValues = new IndividualToken[foundType.size()][];
            for (int index = 0; index < foundType.size(); index++) {
                String key = foundType.getKey(index);

                Token.Individual[] absoluteValue = subj.get(key);

                if (absoluteValue == null || absoluteValue.length == 0)
                    if (parentForValues != null)
                        if (parentForValues.type != null)
                            absoluteValue = parentForValues.getTokens(key);

                if (absoluteValue == null || absoluteValue.length == 0)
                    absoluteValue = foundType.getDefaultValue(index);

                if (absoluteValue == null || absoluteValue.length == 0)
                    complete = false;
                // else
                // System.out.println(Arrays.toString(absoluteValue));

                absolvedValues[index] = absoluteValue;
            }

            return new FormalSettings(subj, absolvedName, foundType, absolvedValues, complete);
        }
    }

    public static abstract class SettingsFormalizerMishap extends AbstractMishap {

        public SettingsFormalizerMishap(Token.Individual token, boolean fatal) {
            super(token, fatal);
        }
    }

    public class BothPrefixAndSuffix extends SettingsFormalizerMishap {

        private final InformalSettings src;

        public BothPrefixAndSuffix(InformalSettings subj) {
            super(subj.name, false);
            this.src = subj;
        }

        @Override
        public String getReport() {
            return "`" + src.getIdentity() + "` asks to be both prefixed and suffixed. Remove one of the dashes at the start or end of its name.";
        }
    }

    public final Maker<LinkedTree<InformalSettings>> treeMaker = new Maker<LinkedTree<InformalSettings>>() {

        @Override
        public LinkedTree<InformalSettings> make(CodeImpl code) {

            CodeProcess making = code.new Process("making a settings tree");

            List<Token.Individual> tokens = DefaultTokenizer.tokenized.read(code);

            Token.Individual[] array = new IndividualToken[tokens.size()];

            int index = 0;
            for (Token.Individual token : tokens)
                array[index++] = token;

            LinkedTree<InformalSettings> tree = new LinkedTree<InformalSettings>();

            int i = 0;

            Stack<Integer> balance = new Stack<Integer>();

            while (i < array.length) {

                // closing a settings node
                if (array[i].is("verbatim <}>")) {

                    tree.goBack();
                    balance.pop();
                    i++;

                }
                // value
                else if (array[i + 1].is("verbatim <:>")) {

                    Token.Individual key = array[i];
                    i++;

                    if (!key.is("identifier")) {
                        making.issue(new UnexpectedToken(key, "identifier", array[i]));
                    }
                    i++;

                    InformalSettings r = tree.getPointer();

                    // boolean referring;
                    // if (array[i].is("verbatim <@>")) {
                    // i++;
                    // referring = true;
                    // } else {
                    // referring = false;
                    // }

                    if (array[i].is("verbatim <[>")) {
                        i++;

                        int j = i;
                        while (!array[j++].is("verbatim <]>"))
                            ;

                        j--;

                        Token.Individual[] a = new IndividualToken[j - i];

                        while (i < j) {
                            a[j - i - 1] = array[i++];
                        }

                        r.set(key.getData(), a);

                    } else {

                        r.set(key.getData(), array[i]);

                    }

                    // if (referring)
                    // r.addReferenceKey(key.data);
                    i++;

                }
                // opening
                else {

                    Token.Individual type, name;
                    String nameString;
                    int openedAt;

                    // opening a nameless and typeless settings node
                    if (array[i].is("verbatim <{>")) {

                        openedAt = i;
                        type = null;
                        name = null;
                        nameString = "<anonymous ?>";
                        i++;

                    }

                    // opening a nameless but typed settings node
                    else if (array[i + 1].is("verbatim <{>")) {

                        type = array[i++];
                        openedAt = i;
                        name = null;
                        nameString = "<anonymous " + type.getData() + ">";
                        i++;

                    }
                    // opening a named and typed settings node
                    else if (array[i + 2].is("verbatim <{>")) {

                        type = array[i++];
                        name = array[i++];
                        openedAt = i;
                        nameString = name.getData();
                        i++;

                    }
                    // unexpected token
                    else {
                        making.issue(new UnexpectedToken(array[i]));
                        i++;
                        continue;
                    }
                    balance.push(openedAt);
                    tree.addAndGoTo(new InformalSettings(type, name));
                }
            }

            while (!balance.isEmpty())
                making.issue(new Unbalanced(array[balance.pop()]));

            return making.finish() ? tree : null;

        }
    };

    public final Maker<Map<? extends Settings, FormalSettings>> mapMaker = new Maker<Map<? extends Settings, FormalSettings>>() {

        @Override
        public synchronized Map<? extends Settings, FormalSettings> make(CodeImpl code) {

            LinkedTree<InformalSettings> tree = treeMaker.make(code);

            CodeProcess formalizing = code.new Process("formalizing those settings");

            Map<String, FormalSettings> namedToFormal = new HashMap<>();

            Map<InformalSettings, FormalSettings> informalToFormal = new HashMap<>();

            Map<Settings, FormalSettings> wrappedToFormal = new HashMap<>();

            FormalSettings nameParent, valuesParent;

            Queue<InformalSettings> q = new LinkedList<>();

            q.add(tree.getRoot());

            while (!q.isEmpty()) {

                // I am dequeued
                InformalSettings informal = q.poll();

                // Enqueue my children so they may also be born
                for (InformalSettings child : tree.getChildren(informal))
                    q.add(child);

                // If I am referring to anyone else in my values, load them all before me

                // Find my syntactically immediate parent in the settings tree
                InformalSettings parent = tree.getParent(informal);

                // I will inherit values, either from my syntactical parent, or from my
                // semantical parent, whom I may be "like"
                if (informal.sets("like")) { // && informal.isReferenceKey("like")) {

                    // inherit from semantical parent
                    String k = informal.get("like")[0].getData(); // TODO multiple semantical parents
                    if (namedToFormal.containsKey(k)) {
                        valuesParent = namedToFormal.get(k); // namedToFormal.get(k).src;
                    }

                    // TODO detect cycles

                    // If my semantical parent has not been born yet,
                    else {

                        // delay my conception.
                        q.add(informal);
                        continue;
                    }
                } else {

                    // inherit from syntactical parent
                    valuesParent = informalToFormal.get(parent);
                }

                // I will take on the name of my syntactical parent,
                nameParent = informalToFormal.get(parent);

                // unless their name does not matter, in which case I will take THEIR
                // syntactical parent's name, so forth.
                while (parent != null && nameParent != null && nameParent.type != null && !nameParent.type.nameMatters) {
                    parent = tree.getParent(parent);
                    nameParent = informalToFormal.get(parent);
                }

                // I am ready to be conceived with my nominal parent and my biological
                // parent.
                CodeProcess absolving = code.new Process("absolving: " + informal.getIdentity());
                FormalSettings formal = absolve(informal, nameParent, valuesParent, absolving);
                absolving.finish();
                informalToFormal.put(informal, formal);
                namedToFormal.put(formal.name, formal);

                // If I am complete,
                if (formal.complete) {

                    // finally make me.
                    formal.wrapping = code.new Process("wrapping: " + formal.getIdentity());
                    Settings wrapped = wrap(formal);
                    formal.wrapping.finish();
                    formal.wrapping = null;
                    if (wrapped != null) {
                        wrappedToFormal.put(wrapped, formal);
                    }
                }
            }
            formalizing.finish();
            return wrappedToFormal;
        }
    };

    class InformalSettings {

        public final Token.Individual type, name;
        private final Map<String, Token.Individual[]> data = new HashMap<>();
        // private final Set<String> referenceKeys = new HashSet<>();

        public InformalSettings(Token.Individual type, Token.Individual name) {
            this.type = type;
            this.name = name;
        }

        public Token.Individual[] get(String key) {
            return data.get(key);
        }

        public String getIdentity() {
            String identity = "";

            if (type == null)
                identity += "<untyped>";
            else
                identity += type.getData();

            if (name == null)
                identity += "<unnamed>";
            else
                identity += " '" + name.getData() + "'";

            return identity;
        }

        @Override
        public String toString() {
            return getIdentity();
        }

        // TODO someone should call this
        // beware of null names and types
        // public String getRepresentation() {
        // String string = name + " as " + type + " (";
        // boolean firstTime = true;
        // for (String key : data.keySet()) {
        // if (firstTime) {
        // firstTime = false;
        // } else {
        // string += ", ";
        // }
        // string += key + ": " + data.get(key);
        // }
        // string += ")";
        // return string;
        // }

        public boolean sets(String key) {
            return data.containsKey(key);
        }

        public void set(String key, Token.Individual... value) {
            data.put(key, value);
        }

        // public void addReferenceKey(String key) {
        // referenceKeys.add(key);
        // }

        // public boolean isReferenceKey(String key) {
        // return referenceKeys.contains(key);
        // }

    }

    public enum Length {
        ONE, ONE_OR_TWO, ONE_OR_MORE
    }

    public Token.Individual DEFAULT_VALUE_NUMERIC_ZERO;
    public Token.Individual DEFAULT_VALUE_NUMERIC_ONE;
    public Token.Individual DEFAULT_VALUE_STRING_DOUBLE_QUOTES;
    public Token.Individual DEFAULT_VALUE_STRING_SINGLE_QUOTES;
    public Token.Individual DEFAULT_VALUE_BOOLEAN_FALSE;
    public Token.Individual DEFAULT_VALUE_BOOLEAN_TRUE;
    public Token.Individual DEFAULT_VALUE_NONE;

    private void setDefaultValues() {

        List<Token.Individual> defaultValues = DefaultTokenizer.tokenized.read(new CodeImpl("0\n1\n\"\"\n''\nfalse true\nnone", this));

        // System.out.println(defaultValues);

        Iterator<Token.Individual> iterator = defaultValues.iterator();

        DEFAULT_VALUE_NUMERIC_ZERO = iterator.next();
        DEFAULT_VALUE_NUMERIC_ONE = iterator.next();
        DEFAULT_VALUE_STRING_DOUBLE_QUOTES = iterator.next();
        DEFAULT_VALUE_STRING_SINGLE_QUOTES = iterator.next();
        DEFAULT_VALUE_BOOLEAN_FALSE = iterator.next();
        DEFAULT_VALUE_BOOLEAN_TRUE = iterator.next();
        DEFAULT_VALUE_NONE = iterator.next();
    }

    abstract class FormalizationBlueprint {

        public final String name;

        public abstract void defineBlueprint();

        public FormalizationBlueprint(String name) {
            this.name = name;
            defineBlueprint();
        }

        private int size = 0;
        private final HashMap<String, Integer> keyToIndex = new HashMap<String, Integer>();
        private final HashMap<Integer, String> indexToKey = new HashMap<Integer, String>();
        private final HashMap<Integer, Token.Individual[]> defaultValues = new HashMap<>();
        private final HashMap<Integer, Length> validLengths = new HashMap<Integer, Length>();
        protected boolean nameMatters = true;

        public void add(String key, Token.Individual... defaultValue) {
            add(key, Length.ONE, defaultValue);
        }

        public void addRange(String key) {
            add(key, Length.ONE_OR_TWO, DEFAULT_VALUE_NUMERIC_ZERO);
            // int | [int, int]
        }

        public void addBoolean(String key, boolean defaultValue) {
            if (defaultValue)
                add(key, DEFAULT_VALUE_BOOLEAN_TRUE);
            else
                add(key, DEFAULT_VALUE_BOOLEAN_FALSE);
        }

        public void add(String key, Length length, Token.Individual... defaultValue) {
            key = key.toLowerCase().replaceAll("( |_)", "-");
            indexToKey.put(size, key);
            keyToIndex.put(key, size);
            defaultValues.put(size, defaultValue);
            validLengths.put(size, length);
            size++;
        }

        public Token.Individual[] getDefaultValue(int index) {
            return defaultValues.get(index);
        }

        public Length getValidLength(int index) {
            return validLengths.get(index);
        }

        public String getKey(int index) {
            return indexToKey.get(index);
        }

        public int getIndex(String key) {
            return keyToIndex.get(key);
        }

        public int size() {
            return size;
        }
    }

    public class FormalSettings {

        public final InformalSettings src;
        public final String name;
        public final FormalizationBlueprint type;
        private final Token.Individual[][] values;
        public final boolean complete;

        private CodeProcess wrapping;

        public FormalSettings(InformalSettings src, String name, FormalizationBlueprint type, Token.Individual[][] absolvedValues, boolean complete) {
            this.src = src;
            this.name = name;
            this.type = type;
            this.values = absolvedValues;
            this.complete = complete;
        }

        public Number getSoleNumeric(String key) {
            return Common.valueOfString(getSoleString(key, false));
        }

        public Number getNumeric(String key, int index) {
            return Common.valueOfString(getStringAt(key, index, false));
        }

        public Boolean getBoolean(String key) {
            String value = getSoleString(key, false);
            switch (value) {
                case "true":
                    return true;
                case "false":
                    return false;
                default:
                    wrapping.issue(new InvalidValue(getTokenAt(key, 0)));
                    return null;
            }
        }

        public Range.Integer getRange(String key) {

            int min, max;

            // extract
            switch (getActualLengthOf(key)) {
                case 1:
                    min = max = (int) getSoleNumeric(key);
                    break;
                case 2:
                    min = (int) getNumeric(key, 0);
                    max = (int) getNumeric(key, 1);
                    break;
                default:
                    min = max = 0;
                    wrapping.issue(new InvalidLength(this, key));
                    return null;
            }

            // validate
            if (max != 0 && (min == 0 || min > max)) {
                wrapping.issue(new InvalidValue(getTokenAt(key, 1)));
                return null;
            }

            if (min == 0)
                min = Range.Integer.INFINITY;

            if (max == 0)
                max = Range.Integer.INFINITY;

            return new Range.Integer(min, max);
        }

        public String getSoleString(String key, boolean escape) {
            if (getActualLengthOf(key) == 1) {
                return getStringAt(key, 0, escape);
            } else {
                wrapping.issue(new InvalidLength(this, key));
                return null;
            }
        }

        public String getStringAt(String key, int index, boolean escape) {
            String s = getTokens(key)[index].getData();
            if (escape)
                return Common.escape(s);
            else
                return s;
        }

        public Token.Individual getTokenAt(String key, int index) {
            return getTokens(key)[index];
        }

        public Token.Individual[] getTokens(String key) {
            return values[type.getIndex(key)];
        }

        public int getActualLengthOf(String key) {
            return values[type.getIndex(key)].length;
        }

        public Length getValidLengthOf(String key) {
            return type.getValidLength(type.getIndex(key));
        }

        public String getIdentity() {
            return type.name + " '" + name + "'";
        }

        @Override
        public String toString() {
            return getIdentity();
        }

        public String getRepresentation() {
            StringBuilder builder = new StringBuilder(name);
            if (type != null) {
                builder.append(" as ").append(type.name).append(" = {");
                for (int index = 0; index < values.length; index++) {
                    if (index > 0)
                        builder.append(", ");
                    builder.append(type.getKey(index)).append(": ").append(Arrays.toString(values[index]));
                }
                builder.append("}");
            }
            return builder.toString();
        }
    }

    static class Unbalanced extends SettingsFormalizerMishap {
        public Unbalanced(Token.Individual token) {
            super(token, true);
        }

        @Override
        public String getReport() {
            return "Unbalanced groups";
        }
    }

    class InvalidLength extends SettingsFormalizerMishap {

        private final String key;
        private final Length validLength;

        public InvalidLength(FormalSettings src, String key) {
            super(src.getTokenAt(key, 2), true);
            this.key = key;
            this.validLength = src.getValidLengthOf(key);
        }

        @Override
        public String getReport() {
            return "length of `" + key + "` must be `" + validLength + "`";
        }
    }

    class InvalidValue extends SettingsFormalizerMishap {

        private final String validValues;

        public InvalidValue(Token.Individual token) {
            this(token, null);
        }

        public InvalidValue(Token.Individual token, String validValues) {
            super(token, true);
            this.validValues = validValues;
        }

        @Override
        public String getReport() {
            String string = "Invalid value: `" + token.getData() + "`";
            if (validValues != null)
                string += ", valid values are: `" + validValues + "`";
            return string;
        }
    }
}
