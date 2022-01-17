package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"SpellCheckingInspection", "SameParameterValue"})
class NodedBinaryTreeImplTest {

    @Test
    void pre1() {
        pre("ABCDEFGH", "CBADHGFE", "EDABCFGH");
    }

    @Test
    void pre2() {
        pre("GFHKDLAWRQPZ", "FGHDALPQRZWK", "KHGFWLDAZRQP");
    }

    @Test
    void post1() {
        post("124536", "425136", "452631");
    }

    @Test
    void in1() {
        in("124895367", "894526731", "849251637");
    }

    void pre(String inOrder, String postOrder, String expectedPreOrder) {
        assertEquals(
                Sequential.of(expectedPreOrder), new PreOrderConstructor<>(
                        Sequential.of(inOrder),
                        Sequential.of(postOrder))
                        .getTarget()
                        .getData());
    }

    void post(String preOrder, String inOrder, String expectedPostOrder) {
        assertEquals(
                Sequential.of(expectedPostOrder), new PostOrderConstructor<>(
                        Sequential.of(preOrder),
                        Sequential.of(inOrder))
                        .getTarget()
                        .getData());
    }

    void in(String preOrder, String postOrder, String expectedInOrder) {
        assertEquals(
                Sequential.of(expectedInOrder), new InOrderConstructor<>(
                        Sequential.of(preOrder),
                        Sequential.of(postOrder))
                        .getTarget()
                        .getData());
    }

}