package ir.smmh.lingu.impl;

import ir.smmh.lingu.*;
import ir.smmh.util.jile.OpenFile;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CodeImpl implements Code {

    public static final Port<List<Token.Individual>> syntax = new Port<>("CodeView:syntax");
    public static final Port<Map<Token.Individual, Set<Mishap>>> mishaps = new Port<>("CodeView:mishaps");
    public static final Port<Map<Token.Individual, OpenFile>> links = new Port<>("CodeView:links");
    private final Language language;
    private final OpenFile openFile;

    public CodeImpl(OpenFile openFile) {
        this(openFile, getLanguage(openFile));
    }

    public CodeImpl(String encoded, Language language) {
        this(OpenFile.of(encoded, ""), language);
    }

    public CodeImpl(String encoded, String ext) {
        this(OpenFile.of(encoded, ext));
    }

    public CodeImpl(@NotNull OpenFile openFile, @NotNull Language language) {
        this.openFile = Objects.requireNonNull(openFile);
        this.language = language;
        beProcessed();
    }

    private static Language getLanguage(OpenFile openFile) {
        String ext = openFile.getExt();
        return ext == null ? TextLanguage.singleton() : Languages.getInstance().getLanguageByExt(ext);
    }

    private synchronized void beProcessed() {
//         String i = getOpenFile().getTitle();
//        out.log("\n" + i + "\n" + "<".repeat(i.length()) + "\n");

        mishaps.write(this, new HashMap<>()); // TODO replace new with clear

        language.getProcessor().process(this);

//        out.log("\n" + i + "\n" + ">".repeat(i.length()) + "\n");
    }

    @Override
    public @NotNull OpenFile getOpenFile() {
        return openFile;
    }

    @Override
    public String toString() {
        return openFile.getTitle();
    }

}