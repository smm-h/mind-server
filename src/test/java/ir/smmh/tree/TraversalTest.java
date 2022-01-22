package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tree.impl.NodedTreeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TraversalTest {

    Tree<Character> tree;

    @BeforeEach
    void beforeEach() {
        NodedTreeImpl<Character> t = new NodedTreeImpl<>();
        NodedTreeImpl<Character>.Node a, b, c;
        t.setRootNode(a = t.new Node('a', null));
        a.getChildren().add(b = t.new Node('b', a));
        a.getChildren().add(c = t.new Node('c', a));
        a.getChildren().add(t.new Node('d', a));
        b.getChildren().add(t.new Node('e', b));
        b.getChildren().add(t.new Node('f', b));
        b.getChildren().add(t.new Node('g', b));
        c.getChildren().add(t.new Node('h', c));
        c.getChildren().add(t.new Node('i', c));
        c.getChildren().add(t.new Node('j', c));
        c.getChildren().add(t.new Node('k', c));
        c.getChildren().add(t.new Node('l', c));
        tree = t;
    }

    @Test
    void traverseBreadthFirst() {
        //noinspection SpellCheckingInspection
        assertEquals(
                Sequential.of("abcdefghijkl"),
                tree.getBreadthFirstData(),
                "Unexpected tree breadth first traversal");
    }

    @Test
    void traverseDepthFirst() {
        //noinspection SpellCheckingInspection
        assertEquals(
                Sequential.of("abefgchijkld"),
                tree.getDepthFirstData(),
                "Unexpected tree depth first traversal");
    }

    @Test
    void toStringTest() {
        assertEquals(
                "a:(b:(e, f, g), c:(h, i, j, k, l), d)",
                tree.toString(),
                "Unexpected tree toString result");
    }

    @Test
    void getSize() {
        assertEquals(
                9,
                tree.getSize(),
                "Unexpected tree size");
    }

    @Test
    void getHeight() {
        assertEquals(
                2,
                tree.getHeight(),
                "Unexpected tree height");
    }

    @Test
    void getDegree() {
        assertEquals(
                5,
                tree.getDegree(),
                "Unexpected tree degree");
    }

    @Test
    void getRootData() {
        assertEquals(
                'a',
                tree.getRootData(),
                "Unexpected tree root data");
    }

    @Test
    void getLeafCount() {
        assertEquals(
                9,
                tree.getBreadth(),
                "Unexpected tree leaf count");
    }

    @Test
    void getLeafData() {
        //noinspection SpellCheckingInspection
        assertEquals(
                Sequential.of("efghijkld"),
                tree.getLeafData(),
                "Unexpected tree leaf data");
    }

    @Test
    void getImmediateSubtrees() {
        assertEquals(
                "[b:(e, f, g), c:(h, i, j, k, l), d]",
                tree.getImmediateSubtrees().toString(),
                "Unexpected tree immediate subtrees");
    }
}
