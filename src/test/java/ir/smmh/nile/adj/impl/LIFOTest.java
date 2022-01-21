package ir.smmh.nile.adj.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LIFOTest {

    private LIFO<Character> lifo;

    @BeforeEach
    void setUp() {
        lifo = new LIFO<>(5);
    }

    @Test
    void getSize_canEnter_enter_clear() {
        assertEquals(0, lifo.getSize());
        assertTrue(lifo.canEnter());
        lifo.enter('A');
        assertEquals(1, lifo.getSize());
        assertTrue(lifo.canEnter());
        lifo.enter('B');
        assertEquals(2, lifo.getSize());
        assertTrue(lifo.canEnter());
        lifo.enter('C');
        assertEquals(3, lifo.getSize());
        assertTrue(lifo.canEnter());
        lifo.enter('D');
        assertEquals(4, lifo.getSize());
        assertTrue(lifo.canEnter());
        lifo.enter('E');
        assertEquals(5, lifo.getSize());
        assertFalse(lifo.canEnter());
        lifo.enter('F');
        assertEquals(5, lifo.getSize());
        assertFalse(lifo.canEnter());
        lifo.clear();
        assertEquals(0, lifo.getSize());
        assertTrue(lifo.canEnter());
    }

    @Test
    void getSize_poll_peek() {
        assertEquals(0, lifo.getSize());
        lifo.enter('A');
        lifo.enter('B');
        lifo.enter('C');
        assertEquals(3, lifo.getSize());
        assertEquals('C', lifo.peek());
        assertEquals('C', lifo.poll());
        assertEquals('B', lifo.peek());
        assertEquals(2, lifo.getSize());
        lifo.enter('D');
        lifo.enter('E');
        assertEquals(4, lifo.getSize());
        lifo.enter('F');
        assertEquals(5, lifo.getSize());
        lifo.enter('G');
        assertEquals(5, lifo.getSize());
        assertEquals('F', lifo.peek());
        assertEquals('F', lifo.poll());
        assertEquals('E', lifo.peek());
        assertEquals('E', lifo.poll());
        assertEquals('D', lifo.peek());
        assertEquals('D', lifo.poll());
        assertEquals('B', lifo.peek());
        assertEquals(2, lifo.getSize());
    }
}