package ir.smmh.mind.api;

import ir.smmh.mind.Property;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicTests {

    MindsAPI api;

    @BeforeEach
    void setUp() {
        api = new MindsAPI();
    }

    @Test
    public void testIntension() {
        final JSONObject request = new JSONObject();
        request.put("method", )
        final JSONObject response = api.process(request);
        a.become(b);
        assertTrue(a.is(b));
    }

    @Test
    public void testTransitivity() {
        a.become(b);
        b.become(c);
        assertTrue(a.is(c));
    }

    @Test
    public void testPossession() {
        final Property p = a.possess("p", t);
        assertTrue(a.has(p));
    }

    @Test
    public void testTransitivePossession() {
        a.become(b);
        final Property p = b.possess("p", t);
        assertTrue(a.has(p));
    }
}