package ir.smmh.nile.adj.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayQueueTest {

    private ArrayQueue<Character> q;

    @BeforeEach
    void setUp() {
        q = new ArrayQueue<>(5);
    }

    @Test
    void getSize_canEnter_enter_clear() {
        assertEquals(0, q.getSize());
        assertTrue(q.canEnter());
        q.enter('A');
        assertEquals(1, q.getSize());
        assertTrue(q.canEnter());
        q.enter('B');
        assertEquals(2, q.getSize());
        assertTrue(q.canEnter());
        q.enter('C');
        assertEquals(3, q.getSize());
        assertTrue(q.canEnter());
        q.enter('D');
        assertEquals(4, q.getSize());
        assertTrue(q.canEnter());
        q.enter('E');
        assertEquals(5, q.getSize());
        assertFalse(q.canEnter());
        q.enter('F');
        assertEquals(5, q.getSize());
        assertFalse(q.canEnter());
        q.clear();
        assertEquals(0, q.getSize());
        assertTrue(q.canEnter());
    }

    @Test
    void getSize_poll_peek() {
        assertEquals(0, q.getSize());
        q.enter('A');
        q.enter('B');
        q.enter('C');
        assertEquals(3, q.getSize());
        assertEquals('A', q.peek());
        assertEquals('A', q.poll());
        assertEquals('B', q.peek());
        assertEquals(2, q.getSize());
        q.enter('D');
        q.enter('E');
        assertEquals(4, q.getSize());
        q.enter('F');
        assertEquals(5, q.getSize());
        q.enter('G');
        assertEquals(5, q.getSize());
        assertEquals('B', q.peek());
        assertEquals('B', q.poll());
        assertEquals('C', q.peek());
        assertEquals('C', q.poll());
        assertEquals('D', q.peek());
        assertEquals('D', q.poll());
        assertEquals('E', q.peek());
        assertEquals(2, q.getSize());
    }
}