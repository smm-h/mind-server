package ir.smmh.mind;

import ir.smmh.mind.impl.MindStorageGate;
import ir.smmh.util.JSONUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings({"ClassNamePrefixedWithPackageName", "ClassWithoutConstructor"})
class MindSerializationTest {

    private static final MindStorageGate gate = new MindStorageGate("test");
    private Mind.Mutable mind;

    @AfterAll
    static void afterAll() {
        // TODO gate.cleanEverything();
    }

    @BeforeEach
    final void beforeEach() {
        mind = gate.createBlank("test-mind");
    }

    @Test
    final void testSerialization() {
        mind.imagine("a");
        JSONObject object = JSONUtil.parse(mind.serialize());
        assertNotNull(object, "JSON parse returned null");
        assertEquals(1, object.getJSONArray("ideas").length(), "There was expected to be a single idea.");
    }
}
