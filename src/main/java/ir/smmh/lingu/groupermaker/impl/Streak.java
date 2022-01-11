package ir.smmh.lingu.groupermaker.impl;

import ir.smmh.jile.common.Range;
import ir.smmh.lingu.groupermaker.GrouperMakerImpl;
import ir.smmh.lingu.settings.FormalSettings;
import ir.smmh.lingu.settings.FormalizationBlueprint;

import java.util.HashSet;
import java.util.Set;

public class Streak extends GrouperMakerImpl.DefinitionImpl {

    public final Range.Integer length;
    public final Boolean ignore;
    private final Formalizer formalizer;
    private final Set<Character> sta, mid, end;

    public Streak(Formalizer formalizer, FormalSettings src) {
        super(src.getName());
        this.formalizer = formalizer;

        length = src.getRange("length");

        // extract sta, mid, and end
        char[] allCan = src.getSoleString("consists-of", true).toCharArray();
        char[] staCan = src.getSoleString("can-start-with", true).toCharArray();
        char[] endCan = src.getSoleString("can-end-with", true).toCharArray();
        char[] staCnt = src.getSoleString("cannot-start-with", true).toCharArray();
        char[] endCnt = src.getSoleString("cannot-end-with", true).toCharArray();

        // TODO pseudo-regex character sets
        //                        .replaceAll("\\[A-Z]", "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        //                        .replaceAll("\\[a-z]", "abcdefghijklmnopqrstuvwxyz")
        //                        .replaceAll("\\[0-9]", "0123456789")

        // if (characters.contains(NEGATOR)) {
        // characters.replace(NEGATOR, "");
        // for (int i = 0; i < 256; i++)
        // this.characters.add((char) i);
        // for (int i = 0; i < characters.length(); i++)
        // this.characters.remove(characters.charAt(i));
        // } else {

        sta = new HashSet<>();
        mid = new HashSet<>();
        end = new HashSet<>();

        for (Character c : allCan) {
            sta.add(c);
            mid.add(c);
            end.add(c);
        }

        for (char c : staCan)
            sta.add(c);
        for (char c : staCnt)
            sta.remove(c);

        for (char c : endCan)
            end.add(c);
        for (char c : endCnt)
            end.remove(c);

        ignore = src.getBoolean("ignore");

    }

    public Set<Character> getSta() {
        return sta;
    }

    public Set<Character> getEnd() {
        return end;
    }

    public Set<Character> getMid() {
        return mid;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    public FormalizationBlueprint getBlueprint() {
        return formalizer.blueprintOfStreak;
    }
}
