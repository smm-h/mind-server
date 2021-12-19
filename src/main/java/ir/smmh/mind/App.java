package ir.smmh.mind;

import ir.smmh.mind.impl.MutableMindImpl;

public class App {
    public static void main(String[] args) {
        final Mind.Mutable mind = new MutableMindImpl();
        final Idea.Mutable a = mind.imagine("a");
        final Idea.Mutable b = mind.imagine("b");
        final Idea.Mutable c = mind.imagine("c");
        a.become(b);
    }
}
