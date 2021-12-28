package ir.smmh.mind;

import ir.smmh.mind.impl.MindStorageGate;
import ir.smmh.util.JSONUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MindSerializationTest {

    private static final MindStorageGate g = new MindStorageGate("test");
    private Mind.Mutable m;

    @BeforeEach
    void setUp() {
        m = g.createBlank("test-mind");
    }

    @Test
    public void testSerializeMind() {
        m.imagine("a");
        JSONObject object = JSONUtil.parse(m.serialize());
        assertNotNull(object);
        assertEquals(1, object.getJSONArray("ideas").length());
    }
}
