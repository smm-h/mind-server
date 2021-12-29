package ir.smmh.mind;

import ir.smmh.util.JSONUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SameParameterValue")
class MindsAPITest {

    MindsAPI api;
    final String mindName = "test";

    @BeforeEach
    void beforeEach() {
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
        return JSONUtil.parse(api.process(request.toString()));
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

    void possess(String idea, String name, String type, JSONObject defaultValue) {
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
        return process("is", parameters).getJSONObject("results").getBoolean("is");
    }

    boolean has(String idea, String name) {
        final JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("idea", idea);
        parameters.put("name", name);
        return process("has", parameters).getJSONObject("results").getBoolean("has");
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
    }

    @Test
    public void testPossession() {
        possess("a", "p", "t", new JSONObject());
        assertTrue(has("a", "p"));
    }

    @Test
    public void testTransitivePossession() {
        become("a", "b");
        possess("b", "p", "t", new JSONObject());
        assertTrue(has("a", "p"));
    }
}