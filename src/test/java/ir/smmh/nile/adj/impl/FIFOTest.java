package ir.smmh.nile.adj.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FIFOTest {

    private FIFO<Character> fifo;

    @BeforeEach
    void setUp() {
        fifo = new FIFO<>(5);
    }

    @Test
    void getSize_canEnter_enter_clear() {
        assertEquals(0, fifo.getSize());
        assertTrue(fifo.canEnter());
        fifo.enter('A');
        assertEquals(1, fifo.getSize());
        assertTrue(fifo.canEnter());
        fifo.enter('B');
        assertEquals(2, fifo.getSize());
        assertTrue(fifo.canEnter());
        fifo.enter('C');
        assertEquals(3, fifo.getSize());
        assertTrue(fifo.canEnter());
        fifo.enter('D');
        assertEquals(4, fifo.getSize());
        assertTrue(fifo.canEnter());
        fifo.enter('E');
        assertEquals(5, fifo.getSize());
        assertFalse(fifo.canEnter());
        fifo.enter('F');
        assertEquals(5, fifo.getSize());
        assertFalse(fifo.canEnter());
        fifo.clear();
        assertEquals(0, fifo.getSize());
        assertTrue(fifo.canEnter());
    }

    @Test
    void getSize_poll_peek() {
        assertEquals(0, fifo.getSize());
        fifo.enter('A');
        fifo.enter('B');
        fifo.enter('C');
        assertEquals(3, fifo.getSize());
        assertEquals('A', fifo.peek());
        assertEquals('A', fifo.poll());
        assertEquals('B', fifo.peek());
        assertEquals(2, fifo.getSize());
        fifo.enter('D');
        fifo.enter('E');
        assertEquals(4, fifo.getSize());
        fifo.enter('F');
        assertEquals(5, fifo.getSize());
        fifo.enter('G');
        assertEquals(5, fifo.getSize());
        assertEquals('B', fifo.peek());
        assertEquals('B', fifo.poll());
        assertEquals('C', fifo.peek());
        assertEquals('C', fifo.poll());
        assertEquals('D', fifo.peek());
        assertEquals('D', fifo.poll());
        assertEquals('E', fifo.peek());
        assertEquals(2, fifo.getSize());
    }
}