package ir.smmh.tree;

import ir.smmh.tree.impl.NodedTreeImpl;
import org.junit.jupiter.api.BeforeEach;

class NodedTreeTest {

    NodedTree<Character, ?, ?> tree;

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
}