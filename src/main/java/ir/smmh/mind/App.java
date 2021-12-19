package ir.smmh.mind;

import ir.smmh.mind.impl.MutableMindImpl;

public class App {
    public static void main(String[] args) {
        final Mind.Mutable mind = new MutableMindImpl();
        final Idea.Mutable a = mind.imagine("a", true);
        final Idea.Mutable b = mind.imagine("b", true);
        final Idea.Mutable c = mind.imagine("c", true);
        a.become(b);
    }
}
