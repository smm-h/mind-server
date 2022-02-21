package ir.smmh.lingu.settings.impl;

import ir.smmh.lingu.CodeProcess;
import ir.smmh.lingu.IndividualTokenType.IndividualToken;
import ir.smmh.lingu.Maker;
import ir.smmh.lingu.Token;
import ir.smmh.lingu.TokenizerMaker;
import ir.smmh.lingu.impl.*;
import ir.smmh.lingu.processors.impl.MultiprocessorImpl;
import ir.smmh.lingu.settings.*;
import ir.smmh.lingu.settings.err.BothPrefixAndSuffix;
import ir.smmh.lingu.settings.err.Unbalanced;
import ir.smmh.tree.impl.NodedTreeImpl;

import java.io.FileNotFoundException;
import java.util.*;

public abstract class SettingsFormalizerImpl<T extends Settings> extends LanguageImpl implements SettingsFormalizer<T> {
    // implements Maker<Map<String, ? extends SettingsFormalizer.Settings>> {

    // Graph Out of A Tree = GOAT

    public static final Maker<NodedTreeImpl<InformalSettings>> treeMaker = code -> {

        CodeProcess making = new CodeProcessImpl(code, "making a settings tree");

        List<Token.Individual> tokens = TokenizerImpl.tokenized.read(code);

        Token.Individual[] array = new IndividualToken[tokens.size()];

        int index = 0;
        for (Token.Individual token : tokens)
            array[index++] = token;

        NodedTreeImpl<InformalSettings> tree = new NodedTreeImpl<>();
        NodedTreeImpl<InformalSettings>.Node node = null;

        int i = 0;

        Stack<Integer> balance = new Stack<>();

        while (i < array.length) {

            // closing a settings node
            if (array[i].is("verbatim <}>")) {

                node = node.getParent();
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

                assert node != null;
                InformalSettings r = node.getData();
                assert r != null;

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
                    while (!array[j].is("verbatim <]>"))
                        j++;

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
                    nameString = "<anonymous ?>"; // TODO assigned but never used
                    i++;

                }

                // opening a nameless but typed settings node
                else if (array[i + 1].is("verbatim <{>")) {

                    type = array[i++];
                    openedAt = i;
                    name = null;
                    nameString = "<anonymous " + type.getData() + ">";  // TODO assigned but never used
                    i++;

                }
                // opening a named and typed settings node
                else if (array[i + 2].is("verbatim <{>")) {

                    type = array[i++];
                    name = array[i++];
                    openedAt = i;
                    nameString = name.getData(); // TODO assigned but never used
                    i++;

                }
                // unexpected token
                else {
                    making.issue(new UnexpectedToken(array[i]));
                    i++;
                    continue;
                }
                balance.push(openedAt);
                node = tree.new Node(new InformalSettingsImpl(type, name), node);
            }
        }

        while (!balance.isEmpty())
            making.issue(new Unbalanced(array[balance.pop()]));

        making.finishMaking();
        return tree;

    };
    public Token.Individual DEFAULT_VALUE_NUMERIC_ZERO, DEFAULT_VALUE_NUMERIC_ONE, DEFAULT_VALUE_STRING_DOUBLE_QUOTES, DEFAULT_VALUE_STRING_SINGLE_QUOTES, DEFAULT_VALUE_BOOLEAN_FALSE, DEFAULT_VALUE_BOOLEAN_TRUE, DEFAULT_VALUE_NONE;

    public SettingsFormalizerImpl(String name, String primaryExt) throws FileNotFoundException, Maker.MakingException {
        this(name, primaryExt, primaryExt);
    }

    public SettingsFormalizerImpl(String name, String langPath, String primaryExt) throws FileNotFoundException, Maker.MakingException {
        super(name, langPath, primaryExt, new MultiprocessorImpl());

        TokenizerImpl tokenizer = TokenizerMaker.getInstance().makeFromTestFile("settings-formalizer");
        getProcessor().extend(tokenizer);

        setDefaultValues();
        setMainMaker(getMapMaker());
    }

    public Maker<Map<T, FormalSettings>> getMapMaker() {
        return code -> {

            NodedTreeImpl<InformalSettings> tree = treeMaker.makeFromCode(code);

            CodeProcess formalizing = new CodeProcessImpl(code, "formalizing those settings");

            Map<T, FormalSettings> wrappedToFormal = new HashMap<>();

            Map<String, FormalSettings> namedToFormal = new HashMap<>();

            Map<InformalSettings, FormalSettings> informalToFormal = new HashMap<>();

            FormalSettings nameParent, valuesParent;

            Queue<InformalSettings> q = new ArrayDeque<>();

            q.add(tree.getRootData());

            while (!q.isEmpty()) {

                // I am dequeued
                InformalSettings informal = q.poll();

                if (informal == null)
                    continue;

                // Enqueue my children so they may also be born
                for (InformalSettings child : tree.findByData(informal).getSingleton().getChildrenData())
                    q.add(child);

                // If I am referring to anyone else in my values, load them all before me

                // Find my syntactically immediate parent in the settings tree
                InformalSettings parent = tree.findByData(informal).getSingleton().getParent().getData();

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
                while (parent != null && nameParent != null && nameParent.getType() != null && !nameParent.getType().doesNameMatter()) {
                    parent = tree.findByData(parent).getSingleton().getParent().getData();
                    nameParent = informalToFormal.get(parent);
                }

                // I am ready to be conceived with my nominal parent and my biological
                // parent.
                CodeProcess absolving = new CodeProcessImpl(code, "absolving: " + informal.getIdentity());
                FormalSettings formal = absolve(informal, nameParent, valuesParent, absolving);
                absolving.finishSilently();
                informalToFormal.put(informal, formal);
                namedToFormal.put(formal.getName(), formal);

                // If I am complete,
                if (formal.isComplete()) {

                    // finally make me.
                    T wrapped = wrap(formal);
                    if (wrapped != null) {
                        wrappedToFormal.put(wrapped, formal);
                    }
                }
            }
            formalizing.finishSilently();
            return wrappedToFormal;
        };
    }

    @Override
    public void setDefaultValues() {

        List<Token.Individual> defaultValues = TokenizerImpl.tokenized.read(new CodeImpl("0\n1\n\"\"\n''\nfalse true\nnone", this));

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

    @Override
    public FormalSettings absolve(InformalSettings subj, FormalSettings parentForName, FormalSettings parentForValues, CodeProcess process) {

        FormalizationBlueprint foundType = subj.getType() == null ? null : findType(subj.getType().getData());

        // absolve name
        String absolvedName;

        if (subj.getName() == null) {
            absolvedName = "";

        } else {
            absolvedName = subj.getName().getData();

            boolean postfix = absolvedName.charAt(0) == '-';
            boolean suffix = absolvedName.charAt(absolvedName.length() - 1) == '-';

            if (postfix && suffix) {
                process.issue(new BothPrefixAndSuffix(subj));
            } else if (postfix) {
                absolvedName = parentForName.getName() + absolvedName;
            } else if (suffix) {
                absolvedName += parentForName.getName();
            }
        }

        if (foundType == null) {
            return new FormalSettingsImpl(subj, absolvedName, null, null, false);

        } else {
            // absolve values
            boolean complete = true;
            Token.Individual[][] absolvedValues = new IndividualToken[foundType.size()][];
            for (int index = 0; index < foundType.size(); index++) {
                String key = foundType.getKey(index);

                Token.Individual[] absoluteValue = subj.get(key);

                if (absoluteValue == null || absoluteValue.length == 0)
                    if (parentForValues != null)
                        if (parentForValues.getType() != null)
                            absoluteValue = parentForValues.getTokens(key);

                if (absoluteValue == null || absoluteValue.length == 0)
                    absoluteValue = foundType.getDefaultValue(index);

                if (absoluteValue == null || absoluteValue.length == 0)
                    complete = false;
                // else
                // System.out.println(Arrays.toString(absoluteValue));

                absolvedValues[index] = absoluteValue;
            }

            return new FormalSettingsImpl(subj, absolvedName, foundType, absolvedValues, complete);
        }
    }

}
