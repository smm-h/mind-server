package ir.smmh.mind;

import ir.smmh.mind.impl.MutableMindImpl;
import ir.smmh.net.api.StandardAPIImpl;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MindsAPI extends StandardAPIImpl {

    private final Map<String, Mind.Mutable> minds = new HashMap<>(8);

    public MindsAPI() {
        defineMethod("mind", (Method) (p) -> {
            String name = p.getString("name");
            if (!minds.containsKey(name)) {
                minds.put(name, MutableMindImpl.createBlank(name, null));
            }
            return maybeOk(NO_ERROR);
        });

        defineMethod("idea", (Method) (p) -> {
            JSONObject r = new JSONObject();
            r.put("code", getIdea(p).encode());
            return ok(r);
        });

        defineMethod("imagine", (Method) (p) -> {
            //noinspection resource
            getMind(p).imagine(p.getString("name"));
            return maybeOk(NO_ERROR);
        });

        defineMethod("become", (Method) (p) -> {
            getIdea(p).become(getIdea(p, "intension"));
            return maybeOk(NO_ERROR);
        });

        defineMethod("possess", (Method) (p) -> {
            getIdea(p).possess(p.getString("name"), p.getString("type"), getMind(p).makeValueGenerator(p.getJSONObject("defaultValue")));
            return maybeOk(NO_ERROR);
        });

        defineMethod("reify", (Method) (p) -> {
            getIdea(p).reify(p.getString("name"), p.getString("type"), Value.of(p.getJSONObject("value"), getMind(p)::findIdeaByName));
            return maybeOk(NO_ERROR);
        });

        int INSTANTIATION_FAILED = 500;
        defineError(INSTANTIATION_FAILED, "Instantiation failed");

        defineMethod("instantiate", (Method) (p) -> {
            Idea.Mutable idea = getIdea(p);
            if (p.has("serialization")) {
                if (idea.instantiate(p.getJSONObject("serialization")) == null) {
                    return maybeOk(INSTANTIATION_FAILED);
                }
            } else {
                idea.instantiate();
            }
            return maybeOk(NO_ERROR);
        });

        defineMethod("is", (Method) (p) -> {
            JSONObject r = new JSONObject();
            r.put("is", getIdea(p).is(getIdea(p, "intension")));
            return ok(r);
        });

        defineMethod("has", (Method) (p) -> {
            JSONObject r = new JSONObject();
            r.put("has", getIdea(p).has(p.getString("name")));
            return ok(r);
        });
    }

    private Mind.Mutable getMind(JSONObject p) {
        return minds.get(p.getString("mind"));
    }

    private Idea.Mutable getIdea(JSONObject p, String key) {
        return getMind(p).findIdeaByName(p.getString(key));
    }

    private Idea.Mutable getIdea(JSONObject p) {
        return getIdea(p, "idea");
    }

    @FunctionalInterface
    interface Method extends ir.smmh.net.api.Method.Plain {
    }
}