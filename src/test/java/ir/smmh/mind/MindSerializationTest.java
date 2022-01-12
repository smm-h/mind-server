package ir.smmh.mind;

import ir.smmh.mind.impl.MindStorageGate;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.Serializable;
import ir.smmh.util.impl.MutableImpl;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MindSerializationTest {

    private static final MindStorageGate g = new MindStorageGate("test");
    private Mind.Mutable m;

    @AfterAll
    static void afterAll() {
        MutableImpl.cleanEverything();
    }

    @BeforeEach
    void beforeEach() {
        m = g.createBlank("test-mind");
    }

    @Test
    public void testSerialization() {
        m.imagine("a");
        JSONObject object = JSONUtil.parse(m.serialize());
        assertNotNull(object);
        assertEquals(1, object.getJSONArray("ideas").length());
    }
}
