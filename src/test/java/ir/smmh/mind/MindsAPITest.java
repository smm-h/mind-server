package ir.smmh.mind;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MindsAPITest {

    MindsAPI api;
    final String mindName = "test";

    @BeforeEach
    void setUp() {
        api = new MindsAPI();
        mind();
        imagine("a");
        imagine("b");
        imagine("c");
        imagine("t");
    }

    JSONObject process(String method, JSONObject parameters) {
        final JSONObject request = new JSONObject();
        request.put("method", method);
        request.put("parameters", parameters);
        return api.process(request);
    }
    
    void mind() {
        final JSONObject parameters = new JSONObject();
        parameters.put("name", mindName);
        process("mind", parameters);
    }

    void imagine(String name) {
        final JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("name", name);
        process("imagine", parameters);
    }

    void become(String idea, String intension) {
        final JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("idea", idea);
        parameters.put("intension", intension);
        process("become", parameters);
    }

    boolean is(String idea, String intension) {
        final JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("idea", idea);
        parameters.put("intension", intension);
        final JSONObject response = process("is", parameters);
        System.out.println(response);
        return response.getJSONObject("results").getBoolean("is");
    }

    boolean has(String idea, String name) {
        final JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("idea", idea);
        parameters.put("name", name);
        final JSONObject response = process("has", parameters);
        return response.getJSONObject("results").getBoolean("has");
    }

    @Test
    public void testIntension() {
        become("a", "b");
        assertTrue(is("a", "b"));
    }

    @Test
    public void testTransitivity() {
        become("a", "b");
        become("b", "c");
        assertTrue(is("a", "c"));
    }

//    @Test
//    public void testPossession() {
//        final Property p = a.possess("p", t);
//        assertTrue(a.has(p));
//    }
//
//    @Test
//    public void testTransitivePossession() {
//become("a", "b");
//        final Property p = b.possess("p", t);
//        assertTrue(a.has(p));
//    }
}