package ir.smmh.lingu.impl;

import ir.smmh.lingu.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CodeProcessImpl implements CodeProcess {

    private final @NotNull Code code;
    private final @NotNull String name;
    private final List<Mishap> myMishaps = new LinkedList<>();
    private boolean safe = true;

    private boolean finished = false;

    public CodeProcessImpl(@NotNull Code code, @NotNull String name) {
        this.code = code;
        this.name = name;
        String i = name + ": " + getCode().getOpenFile().getTitle();
        System.out.println("\n\t" + i + "\n\t" + "<".repeat(i.length()) + "\n");

    }

    @Override
    public void issue(@NotNull Mishap mishap) {
        if (finished) {
            System.out.println("Tried to add a mishap.");
        } else {
            mishap.setProcess(this);
            if (safe) {
                if (mishap.isFatal()) {
                    safe = false;
                }
            }
            myMishaps.add(mishap);
            // if (mishap.fatal) System.err.println("\t" + mishap.toString());
        }
    }

    public @NotNull Code getCode() {
        return code;
    }

    @Override
    public boolean finishSilently() {
        try {
            finishMaking();
            return true;
        } catch (Maker.MakingException e) {
            return false;
        }
    }

    @Override
    public void finishMaking() throws Maker.MakingException {

        finished = true;

        String i = name + ": " + getCode().getOpenFile().getTitle();
        System.out.println("\n\t" + i + "\n\t" + ">".repeat(i.length()) + "\n");

        if (!safe) {
            System.out.println("" + myMishaps.size() + " mishap(s) during: '" + name + "' of: " + this);
            for (Mishap mishap : myMishaps)
                System.err.println("\t" + mishap.toString());

            Map<Token.Individual, Set<Mishap>> map = CodeImpl.mishaps.read(getCode());

            for (Mishap mishap : myMishaps) {
                if (mishap instanceof Mishap.Caused) {
                    Token.Individual key = ((Mishap.Caused) mishap).getCause();
                    if (!map.containsKey(key)) {
                        map.put(key, new HashSet<>());
                    }
                    map.get(key).add(mishap);
                }
            }
            throw new Maker.MakingException();
        }
    }
}
