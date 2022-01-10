package ir.smmh.lingu.impl;


import ir.smmh.lingu.CodeProcess;
import ir.smmh.lingu.Maker;
import ir.smmh.lingu.Token;
import ir.smmh.tree.jile.impl.LinkedTree;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

public class TreeLanguage extends LanguageImpl {

    private static TreeLanguage singleton;
    public final Maker<LinkedTree<String>> treeMaker = code -> {
        CodeProcess process = new CodeProcessImpl(code, "making a tree");
        LinkedTree<String> tree = new LinkedTree<>();
        Token.Individual token;
        // Token valueToken;
        // String last;
        List<Token.Individual> tokens = TokenizerImpl.tokenized.read(code);
        // System.out.println(tokens.toString().replaceAll(", ", ""));
        for (Token.Individual individual : tokens) {
            token = individual;
            switch (token.getTypeString()) {
                case "verbatim <{>":
                    tree.goToLastAdded();
                    break;
                case "verbatim <}>":
                    tree.goBack();
                    break;
                case "node":
                    tree.add(token.getData());
                    break;
                // case "verbatim <hide>":
                // tree.setSelectedNodeVisibility(false);
                // break;
                // case "verbatim <root>":
                // tree.viewSelectedNodeAsRoot();
                // break;
                // case "node":
                // last = token.data;
                // tree.addAndSelect(last);
                // break;
                // case "letters": // "identifier"?
                // valueToken = iterator.next();
                // try {
                // tree.setAttribute(token.data, Integer.parseInt(valueToken.data));
                // } catch (NumberFormatException e) {
                // process.add(new Tokenizer.Mishap.UnexpectedToken(token, valueToken.data));
                // } catch (NoSuchFieldException e) {
                // process.add(new Tokenizer.Mishap.UnexpectedToken(token));
                // }
                // break;
                default:
                    process.issue(new UnexpectedToken(token));
            }
        }
        process.finishMaking();
            return tree;
    };

    private TreeLanguage() throws FileNotFoundException, Maker.MakingException {
        super("Tree Language", "tlg", Objects.requireNonNull(TokenizerMaker.singleton().makeFromTestFile("tree-language")));

        setMainMaker(treeMaker);
    }

    public static TreeLanguage singleton() throws FileNotFoundException, Maker.MakingException {
        if (singleton == null) {
            singleton = new TreeLanguage();
        }
        return singleton;
    }
}