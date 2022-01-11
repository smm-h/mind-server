package ir.smmh.lingu.impl;

import ir.smmh.lingu.*;
import ir.smmh.tree.jile.impl.LinkedTree;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.List;

import static ir.smmh.lingu.settings.impl.SettingsFormalizerImpl.treeMaker;

public class TreeLanguage extends LanguageImpl implements Maker<LinkedTree<String>> {

    public TreeLanguage() throws FileNotFoundException, Maker.MakingException {
        super("Tree Language", "tlg", Languages.getInstance().getTokenizerMaker().makeFromTestFile("tree-language"));

        setMainMaker(treeMaker);
    }

    @Override
    public @NotNull LinkedTree<String> makeFromCode(@NotNull Code code) throws MakingException {

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
    }
}