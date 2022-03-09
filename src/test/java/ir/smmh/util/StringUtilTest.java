package ir.smmh.util;

import org.junit.jupiter.api.Test;

import static ir.smmh.util.StringUtil.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SpellCheckingInspection")
class StringUtilTest {

    @Test
    void shiftTest() {
        String s = "a\nb";
        assertEquals("\ta\n\tb", tabIn(s));
        assertEquals("  a\n  b", shiftRight(s, 2));
        assertEquals("--a\n--b", shiftRight(s, 2, '-'));
    }

    @Test
    void repeatTest() {
        assertEquals("%%%", repeat('%', 3));
    }

    @Test
    void symbolValueConversionTest() {
        assertEquals(9, valueOfSymbol('9'));
        assertEquals(10, valueOfSymbol('a'));
        assertEquals(15, valueOfSymbol('f'));
        assertEquals("123456789", stringOfValue(123456789));
        assertEquals(123456789, valueOfString("123456789"));
        assertEquals(16, valueOfSymbol('g'));
        assertThrows(IllegalArgumentException.class, () -> valueOfSymbol('g', (byte) 16));
        assertEquals('Z', symbolOfValue((byte) 35));
        assertEquals("A", stringOfValue(10, (byte) 11));
        assertEquals(10, valueOfString("A", (byte) 11));
        assertEquals("10", stringOfValue(11, (byte) 11));
        assertEquals(11, valueOfString("10", (byte) 11));
    }

    @Test
    void fillTest() {
        assertEquals(" 25", fill("25", " ", true, 3, false));
        assertEquals("25 ", fill("25", " ", false, 3, false));
        assertEquals("25", fill("25", " ", true, 1, false));
        assertEquals("25", fill("25", " ", false, 1, false));
        assertEquals("2", fill("25", " ", false, 1, true));
        assertEquals("5", fill("25", " ", true, 1, true));
    }

    @Test
    void codepointToTextTest() {
        assertEquals("U+0041", codepointToText(65));
    }

    @Test
    void containsTest() {
        assertTrue(contains("hey", 'e'));
        assertFalse(contains("hey", 'a'));
    }

    @Test
    void countTest() {
        assertEquals(0, count("yoohoo", 'x'));
        assertEquals(1, count("yoohoo", 'y'));
        assertEquals(4, count("yoohoo", 'o'));
    }

    @Test
    void splitTest() {
        String s = "\na\nbcd\nefgh\n\nij";
        assertEquals("[, a, bcd, efgh, , ij]", splitByCharacter(s, '\n').toString());
        assertEquals("[\na\n, bcd, \nef, gh\n, \nij]", splitByLength(s, 3).toString());
    }

    @Test
    void replaceCharacterTest() {
        assertEquals("heehee", replaceCharacter("hoohoo", 'o', 'e'));
    }

    @Test
    void removePrefixTest() {
        assertEquals("World", removePrefix("HelloWorld", "Hello"));
    }
}