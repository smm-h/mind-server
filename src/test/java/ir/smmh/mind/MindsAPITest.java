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
        imagine("d");
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

    void possess(String idea, String name, String type, String defaultValue) {
        final JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("idea", idea);
        parameters.put("name", name);
        parameters.put("type", type);
        parameters.put("defaultValue", defaultValue);
        process("possess", parameters);
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
        System.out.println(response);
        return response.getJSONObject("results").getBoolean("has");
    }

    String idea(String idea) {
        final JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("idea", idea);
        return process("idea", parameters).getJSONObject("results").getString("code");
    }

    @Test
    public void testIntension() {
        become("a", "b");
        assertTrue(is("a", "b"));
    }

    @Test
    public void testTransitivity() {
        become("c", "d");
        become("b", "c");
        become("a", "b");
        assertTrue(is("a", "c"));
        assertTrue(is("b", "d"));
        assertTrue(is("a", "d"));
        System.out.println(idea("a"));
    }

    @Test
    public void testPossession() {
        possess("a", "p", "t", "null");
        System.out.println(idea("a"));
        assertTrue(has("a", "p"));
    }

    @Test
    public void testTransitivePossession() {
        become("a", "b");
        possess("b", "p", "t", "null");
        assertTrue(has("a", "p"));
    }
}