package ir.smmh.mind;

import ir.smmh.util.JSONUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings({"SameParameterValue", "ClassWithoutConstructor", "ClassNamePrefixedWithPackageName", "ClassWithTooManyMethods"})
class MindsAPITest {

    private static final String mindName = "test";
    private MindsAPI api;

    @BeforeEach
    final void beforeEach() {
        api = new MindsAPI();
        mind();
        imagine("a");
        imagine("b");
        imagine("c");
        imagine("d");
        imagine("t");
    }

    final JSONObject process(String method, JSONObject parameters) {
        JSONObject request = new JSONObject();
        request.put("method", method);
        request.put("parameters", parameters);
        return JSONUtil.parse(api.process(request.toString()));
    }

    final void mind() {
        JSONObject parameters = new JSONObject();
        parameters.put("name", mindName);
        process("mind", parameters);
    }

    final void imagine(String name) {
        JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("name", name);
        process("imagine", parameters);
    }

    final void become(String idea, String intension) {
        JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("idea", idea);
        parameters.put("intension", intension);
        process("become", parameters);
    }

    final void possess(String idea, String name, String type, JSONObject defaultValue) {
        JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("idea", idea);
        parameters.put("name", name);
        parameters.put("type", type);
        parameters.put("defaultValue", defaultValue);
        process("possess", parameters);
    }

    final boolean is(String idea, String intension) {
        JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("idea", idea);
        parameters.put("intension", intension);
        return process("is", parameters).getJSONObject("results").getBoolean("is");
    }

    final boolean has(String idea, String name) {
        JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("idea", idea);
        parameters.put("name", name);
        return process("has", parameters).getJSONObject("results").getBoolean("has");
    }

    final String idea(String idea) {
        JSONObject parameters = new JSONObject();
        parameters.put("mind", mindName);
        parameters.put("idea", idea);
        return process("idea", parameters).getJSONObject("results").getString("code");
    }

    @Test
    final void testIntension() {
        become("a", "b");
        System.out.println(idea("a"));
        assertTrue(is("a", "b"), "a is not b");
    }

    @Test
    final void testTransitivity() {
        become("c", "d");
        become("b", "c");
        become("a", "b");
        assertTrue(is("a", "c"), "a is not c");
        assertTrue(is("b", "d"), "b is not d");
        assertTrue(is("a", "d"), "a is not d");
    }

    @Test
    final void testPossession() {
        possess("a", "p", "t", new JSONObject());
        assertTrue(has("a", "p"), "a does not have p");
    }

    @Test
    final void testTransitivePossession() {
        become("a", "b");
        possess("b", "p", "t", new JSONObject());
        assertTrue(has("a", "p"), "a does not have p");
    }
}