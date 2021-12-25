package ir.smmh.mind;

import ir.smmh.api.StandardAPI;
import ir.smmh.mind.impl.MutableMindImpl;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MindsAPI extends StandardAPI {

    public interface Method extends ir.smmh.api.Method.Plain {
    }

    private final Map<String, Mind.Mutable> minds = new HashMap<>();

    private Mind.Mutable getMind(JSONObject p) {
        return minds.get(p.getString("mind"));
    }

    private Idea.Mutable getIdea(JSONObject p, String key) {
        return getMind(p).findIdeaByName(p.getString(key));
    }

    private Idea.Mutable getIdea(JSONObject p) {
        return getIdea(p, "idea");
    }

    public MindsAPI() {
        defineMethod("mind", (Method) (p) -> {
            final String name = p.getString("name");
            if (!minds.containsKey(name)) {
                minds.put(name, new MutableMindImpl(name, null));
            }
            return respond(NO_ERROR);
        });

        defineMethod("idea", (Method) (p) -> {
            JSONObject r = new JSONObject();
            r.put("code", getIdea(p).encode());
            return respond(r);
        });

        defineMethod("imagine", (Method) (p) -> {
            getMind(p).imagine(p.getString("name"));
            return respond(NO_ERROR);
        });

        defineMethod("become", (Method) (p) -> {
            getIdea(p).become(getIdea(p, "intension"));
            return respond(NO_ERROR);
        });

        defineMethod("possess", (Method) (p) -> {
            getIdea(p).possess(p.getString("name"), getIdea(p, "type"), getMind(p).makeValueGenerator(p.getJSONObject("defaultValue")));
            return respond(NO_ERROR);
        });

        defineMethod("reify", (Method) (p) -> {
            getIdea(p).reify(p.getString("name"), getIdea(p, "type"), Value.of(p.getJSONObject("value"), getMind(p)::findIdeaByName));
            return respond(NO_ERROR);
        });

        int INSTANTIATION_FAILED = defineError(126, "Instantiation failed");

        defineMethod("instantiate", (Method) (p) -> {
            final Idea.Mutable i = getIdea(p);
            if (p.has("serialization")) {
                if (i.instantiate(p.getJSONObject("serialization")) == null) {
                    return respond(INSTANTIATION_FAILED);
                }
            } else {
                i.instantiate();
            }
            return respond(NO_ERROR);
        });

        defineMethod("is", (Method) (p) -> {
            JSONObject r = new JSONObject();
            r.put("is", getIdea(p).is(getIdea(p, "intension")));
            return respond(r);
        });

        defineMethod("has", (Method) (p) -> {
            JSONObject r = new JSONObject();
            r.put("has", getIdea(p).has(p.getString("name")));
            return respond(r);
        });
    }
}