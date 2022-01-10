package ir.smmh.lingu.impl;

import ir.smmh.lingu.*;
import ir.smmh.lingu.CollectiveTokenType.CollectiveToken;
import ir.smmh.lingu.groupermaker.GrouperMakerImpl;
import ir.smmh.lingu.groupermaker.impl.*;
import ir.smmh.lingu.groupermaker.GrouperMakerImpl.DefinitionImpl;
import ir.smmh.lingu.processors.SingleProcessor;
import ir.smmh.lingu.settings.FormalSettings;
import ir.smmh.tree.jile.Tree;
import org.jetbrains.annotations.NotNull;

import java.util.*;
// import ir.smmh.jile.common.Convertor;
// import ir.smmh.tree.jile.TraversibleTree;

public class GrouperImpl extends SingleProcessor implements Grouper {

    private final Map<String, GroupType> groupTypes = new HashMap<>();
    private final TokenizerImpl tokenizer = new TokenizerImpl();

    private final Map<GrouperMakerImpl.Definition, FormalSettings> sources;
    private final PriorityQueue<GrouperMakerImpl.Definition> schedule = new PriorityQueue<>(Comparator.comparingInt(GrouperMakerImpl.Definition::getPriority));
    private GroupType rootGroupType = null;

    public GrouperImpl(Map<GrouperMakerImpl.Definition, FormalSettings> sources) {

        this.sources = sources;

        for (GrouperMakerImpl.Definition key : sources.keySet())
            schedule(key);

        defineAsScheduled();
    }

    private FormalSettings findSourceForDefinition(DefinitionImpl definition) {
        return sources.get(definition);
    }

    @Override
    public void process(@NotNull Code code) {

        tokenizer.process(code);

        CodeProcess planning = new CodeProcessImpl(code, "planning-to-group");

        List<Token.Individual> tokens = TokenizerImpl.tokenized.read(code);

        if (groupTypes.isEmpty())
            planning.issue(new NoGroupTypeDefined());

        if (rootGroupType == null)
            planning.issue(new NoRootGroupType());

        Map<Integer, Deepening> deepeningDuties = new HashMap<>();

        Map<Integer, CollectiveToken> groupingDuties = new HashMap<>();

        Stack<Integer> opened = new Stack<>();

        outer:
        for (GroupType type : groupTypes.values()) {
            int separatorCount = 0;
            Stack<Token.Individual> openerStack = new Stack<>();
            TokenizerImpl.Verbatim separator = null;
            for (int index = 0; index < tokens.size(); index++) {
                Token.Individual token = tokens.get(index);
                if (token.getType() instanceof TokenizerImpl.Verbatim) {
                    TokenizerImpl.Verbatim verbatim = (TokenizerImpl.Verbatim) token.getType();
                    if (verbatim.equals(type.opener)) {
                        openerStack.push(token);
                        opened.push(index);
                    } else if (verbatim.equals(type.closer)) {
                        int openedIndex = opened.pop();
                        deepeningDuties.put(openedIndex, Deepening.OPEN);
                        // deepeningDuties.put(openedIndex, Deepening.OPEN_AND_SPLIT);
                        deepeningDuties.put(index, Deepening.CLOSE);
                        // deepeningDuties.put(index, Deepening.STITCH_AND_CLOSE);
                        groupingDuties.put(openedIndex, type.new CollectiveToken(openerStack.pop(), token));
                    } else if (verbatim.equals(type.separator)) {
                        if (separatorCount == 0) {
                            separator = verbatim;
                        } else {
                            if (!verbatim.equals(separator)) {
                                planning.issue(new TwoDifferentSeparators(token, groupingDuties.get(opened.peek()), verbatim, separator));
                                break outer;
                            }
                        }
                        // deepeningDuties.put(index, Deepening.STITCH_AND_SPLIT);
                        separatorCount++;
                        // TODO apply separation constraints
                    }
                }
            }
            if (!opened.isEmpty()) {
                planning.issue(new Unbalanced(tokens.get(opened.peek())));
            }
        }

        // TokenType cellType; TODO

        if (planning.finishSilently()) {

            CodeProcess grouping = new CodeProcessImpl(code, "grouping");

            // System.out.println(groupingDuties);
            // System.out.println(deepeningDuties);

            // LinkedTree<CollectiveToken> tree = new LinkedTree<CollectiveToken>();
            // tree.addAndGoTo(rootGroupType.new CollectiveToken(0, code.getSize() - 1));

            Stack<CollectiveToken> roots = new Stack<>();
            roots.push(rootGroupType.new CollectiveToken());

            // LinkedTree<Token> vtree = new LinkedTree<Token>();
            // vtree.addAndGoTo(new VirtualToken("<ROOT>"));

            Stack<CollectiveToken> groups = new Stack<>();

            for (int index = 0; index < tokens.size(); index++) {
                // System.out.println(index);
                Token.Individual token = tokens.get(index);
                if (groupingDuties.containsKey(index)) {
                    CollectiveToken duty = groupingDuties.get(index);
                    if (duty == null) {
                        groups.pop();
                    } else {
                        groups.push(duty);
                    }
                }
                if (deepeningDuties.containsKey(index)) {
                    switch (deepeningDuties.get(index)) {
                        case OPEN:
                            // tree.addAndGoTo(groups.peek());
                            roots.peek().add(groups.peek());
                            roots.push(groups.peek());
                            break;
                        case CLOSE:
                            // tree.goBack();
                            roots.pop();
                            break;
                        // case OPEN_AND_SPLIT:
                        // groups.peek().split();
                        // tree.addAndGoTo(groups.peek());
                        // break;
                        // case STITCH_AND_SPLIT:
                        // groups.peek().split();
                        // break;
                        // case STITCH_AND_CLOSE:
                        // tree.goBack();
                        // break;
                    }
                } else {
                    roots.peek().add(token);
                    // tree.getPointer().add(token);
                }
            }

            if (grouping.finishSilently()) {

                CollectiveToken root = roots.pop();

                GrouperImpl.grouped.write(code, root);

                Tree<Token> t = root.toTree();

                // TreeView.port.write(code, ((TraversibleTree<Token>) t).convert(new
                // Convertor<Token, String>() {

                // @Override
                // public String convert(Token source) {
                // return source.toString();
                // }

                // }));
            }
        }
    }

    public void schedule(GrouperMakerImpl.Definition definition) {
        schedule.add(definition);
    }

    public void defineAsScheduled() {
        while (!schedule.isEmpty()) {
            GrouperMakerImpl.Definition definition = schedule.poll();
            // System.out.println("::: " + definition.getPriority() + "\t" + definition.src.name);
            define(definition);
        }
    }

    public void define(GrouperMakerImpl.Definition definition) {

        if (definition instanceof Metadata)
            define((Metadata) definition);

        else if (definition instanceof Streak)
            define((Streak) definition);

        else if (definition instanceof Multitude)
            define((Multitude) definition);

        else if (definition instanceof Pattern)
            define((Pattern) definition);

        else if (definition instanceof RelativePattern)
            define((RelativePattern) definition);

        else throw new RuntimeException("undefined");
    }

    public void define(Metadata d) {
        rootGroupType = groupTypes.get(d.root);
        System.out.println("Root group type was set");
        // for (int i = 0; i < m.exts.length; i++)
        // Languages.getInstance().associateExtWithLanguage(m.exts[i], language);
    }

    public void define(Streak d) {

        tokenizer.define(new TokenizerMakerImpl.Streak(d.getAbsoluteName(), d.getMid()));

        if (d.ignore)
            if (!tokenizer.ignore(d.getAbsoluteName()))
                // TODO turn this into a mishap
                System.out.println("Could not ignore: " + d.getAbsoluteName());
    }

    public void define(Multitude d) {

        if (d.opaque) {

            tokenizer.define(new TokenizerMakerImpl.Kept(d.getAbsoluteName(), d.opener, d.closer));

            if (!d.separator.equals("none"))
                new MultitudeOpaqueButSeparated(findSourceForDefinition(d));

            if (d.ignore)
                if (!tokenizer.ignore(d.getAbsoluteName()))
                    // TODO turn this into a mishap
                    System.out.println("Could not ignore: " + d.getAbsoluteName());

        } else {

            TokenizerImpl.Verbatim opener = tokenizer.define(new TokenizerMakerImpl.Verbatim(d.opener));
            TokenizerImpl.Verbatim closer = tokenizer.define(new TokenizerMakerImpl.Verbatim(d.closer));
            TokenizerImpl.Verbatim separator = tokenizer.define(new TokenizerMakerImpl.Verbatim(d.separator));
            groupTypes.put(d.getAbsoluteName(), new GroupType(d.getAbsoluteName(), opener, closer, separator, null));

            if (d.ignore)
                new MultitudeNotOpaqueButIgnored(findSourceForDefinition(d));
        }
    }

    // public final Boolean precedence;
    // public final String[] pattern;
    // public final boolean[] isVerbatim;
    public void define(Pattern d) {

        for (int i = 0; i < d.pattern.length; i++) {
            if (d.isVerbatim[i]) {
                tokenizer.define(new TokenizerMakerImpl.Verbatim(d.pattern[i]));
            }
        }
    }

    public void define(RelativePattern d) {

    }

    enum Deepening {
        OPEN, CLOSE // , OPEN_AND_SPLIT, STITCH_AND_CLOSE, STITCH_AND_SPLIT
    }

    public static class GroupType extends CollectiveTokenType implements Comparable<GroupType> {

        public final TokenizerImpl.Verbatim opener, closer, separator;

        public final IndividualTokenType cellType;

        public GroupType(String name, TokenizerImpl.Verbatim opener, TokenizerImpl.Verbatim closer, TokenizerImpl.Verbatim separator, IndividualTokenType cellType) {
            super("group_type <" + name + ">", opener.data + "..." + closer.data);
            this.opener = opener;
            this.closer = closer;
            this.separator = separator;
            this.cellType = cellType;
        }

        @Override
        public int compareTo(GroupType other) {
            int comparison = -Integer.compare(this.opener.data.length(), other.opener.data.length());
            if (comparison == 0)
                comparison = this.opener.compareTo(other.opener);
            return comparison;
        }
    }

    public static class Unbalanced extends MishapImpl.Caused {

        public Unbalanced(Token.Individual opener) {
            super(opener, true);
            assert opener.getType() instanceof TokenizerImpl.Verbatim;
        }

        @Override
        public String getReport() {
            return "Opened but not closed: `" + token.getData() + "`";
        }
    }

    public static class MultitudeOpaqueButSeparated extends MishapImpl.Caused {

        private final FormalSettings src;

        public MultitudeOpaqueButSeparated(FormalSettings src) {
            super(src.getTokenAt("separator", 0), false);
            this.src = src;
        }

        @Override
        public String getReport() {
            return "`" + src.getIdentity() + "` cannot both be opaque and have a separator. Change this to `none`, or delete it.";
        }
    }

    public static class MultitudeNotOpaqueButIgnored extends MishapImpl.Caused {

        private final FormalSettings src;

        public MultitudeNotOpaqueButIgnored(FormalSettings src) {
            super(src.getTokenAt("ignore", 0), false);
            this.src = src;
        }

        @Override
        public String getReport() {
            return src.getIdentity() + " cannot both be non-opaque and ignored. Change this to `false`, or delete it, or change `opaque` to `true`.";
        }
    }

    public static class NoGroupTypeDefined extends MishapImpl {
        public NoGroupTypeDefined() {
            super(true);
        }

        @Override
        public String getReport() {
            return "No group types are defined";
        }
    }

    public static class NoRootGroupType extends MishapImpl {
        public NoRootGroupType() {
            super(true);
        }

        @Override
        public String getReport() {
            return "Root group type is not set";
        }
    }

    public class TwoDifferentSeparators extends MishapImpl.Caused {
        final CollectiveToken group;
        final TokenizerImpl.Verbatim one, another;

        public TwoDifferentSeparators(Token.Individual token, CollectiveToken group, TokenizerImpl.Verbatim first, TokenizerImpl.Verbatim second) {
            super(token, true);
            this.group = group;
            this.one = first;
            this.another = second;
        }

        @Override
        public String getReport() {
            return "The group `" + group.toString() + "` was separated by more than one separators";
        }
    }
}
