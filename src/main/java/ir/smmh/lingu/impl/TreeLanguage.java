package ir.smmh.lingu.impl;

import ir.smmh.jile.common.Singleton;
import ir.smmh.lingu.CodeProcess;
import ir.smmh.lingu.Language;
import ir.smmh.lingu.Linter;
import ir.smmh.lingu.Token;
import ir.smmh.tree.jile.impl.LinkedTree;

import java.util.List;

public class TreeLanguage extends Language implements Singleton, Linter {

    private static TreeLanguage singleton;

    public static TreeLanguage singleton() {
        if (singleton == null) {
            singleton = new TreeLanguage();
        }
        return singleton;
    }

    private TreeLanguage() {
        super("Tree Language", "tlg", TokenizerMaker.singleton().maker.makeFrom("tree-language"));

        setMainMaker(treeMaker);
    }

    public final Maker<LinkedTree<String>> treeMaker = new Maker<LinkedTree<String>>() {
        @Override
        public LinkedTree<String> make(CodeImpl code) {
            CodeProcess process = code.new Process("making a tree");
            LinkedTree<String> tree = new LinkedTree<String>();
            Token.Individual token;
            // Token valueToken;
            // String last;
            List<Token.Individual> tokens = DefaultTokenizer.tokenized.read(code);
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
            if (process.finish()) {
                return tree;
            } else {
                return null;
            }
        }
    };
}