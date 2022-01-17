package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tree.NodedTree;
import ir.smmh.tree.Tree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
class NodedBinaryTreeImplTest {

    @Test
    void testPreOrderFromInOrderAndPostOrder2() {
        testPreOrderFromInOrderAndPostOrder("ABCDEFGH", "CBADHGFE", "EDABCFGH");
    }

    @Test
    void testPreOrderFromInOrderAndPostOrder1() {
        testPreOrderFromInOrderAndPostOrder("GFHKDLAWRQPZ", "FGHDALPQRZWK", "KHGFWLDAZRQP");
    }

    void testPreOrderFromInOrderAndPostOrder(String inOrder, String postOrder, String expectedPreOrder) {
        Tree<Character> tree = NodedBinaryTreeImpl.fromInOrderAndPostOrder(
                Sequential.of(inOrder),
                Sequential.of(postOrder));
        System.out.println(tree);

        Tree.TraversedData<Character> preOrder = tree.traverseData(NodedTree.DataTraversal.Binary.PRE_ORDER);
        System.out.println(preOrder);

        assertEquals(preOrder.getData(), Sequential.of(expectedPreOrder));
    }

}