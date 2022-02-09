package ir.smmh.nile.adj.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayStackTest {

    private ArrayStack<Character> s;

    @BeforeEach
    void setUp() {
        s = new ArrayStack<>(5);
    }

    @Test
    void getSize_canEnter_enter_clear() {
        assertEquals(0, s.getSize());
        assertTrue(s.canEnter());
        s.enter('A');
        assertEquals(1, s.getSize());
        assertTrue(s.canEnter());
        s.enter('B');
        assertEquals(2, s.getSize());
        assertTrue(s.canEnter());
        s.enter('C');
        assertEquals(3, s.getSize());
        assertTrue(s.canEnter());
        s.enter('D');
        assertEquals(4, s.getSize());
        assertTrue(s.canEnter());
        s.enter('E');
        assertEquals(5, s.getSize());
        assertFalse(s.canEnter());
        s.enter('F');
        assertEquals(5, s.getSize());
        assertFalse(s.canEnter());
        s.clear();
        assertEquals(0, s.getSize());
        assertTrue(s.canEnter());
    }

    @Test
    void getSize_poll_peek() {
        assertEquals(0, s.getSize());
        s.enter('A');
        s.enter('B');
        s.enter('C');
        assertEquals(3, s.getSize());
        assertEquals('C', s.peek());
        assertEquals('C', s.poll());
        assertEquals('B', s.peek());
        assertEquals(2, s.getSize());
        s.enter('D');
        s.enter('E');
        assertEquals(4, s.getSize());
        s.enter('F');
        assertEquals(5, s.getSize());
        s.enter('G');
        assertEquals(5, s.getSize());
        assertEquals('F', s.peek());
        assertEquals('F', s.poll());
        assertEquals('E', s.peek());
        assertEquals('E', s.poll());
        assertEquals('D', s.peek());
        assertEquals('D', s.poll());
        assertEquals('B', s.peek());
        assertEquals(2, s.getSize());
    }
}