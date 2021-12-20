package ir.smmh.mind.api;

import ir.smmh.api.StandardAPI;
import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import ir.smmh.mind.impl.MutableMindImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MindsAPI extends StandardAPI<Method> {
    @Override
    public @Nullable Method findMethod(@NotNull String methodName) {
        switch (methodName) {
            case "mind":
                return mind;
            case "imagine":
                return imagine;
            case "become":
                return become;
            case "possess":
                return possess;
            case "reify":
                return reify;
            case "instantiate":
                return instantiate;
            default:
                return null;
        }
    }

    private final Map<String, Mind.Mutable> minds = new HashMap<>();

    private Mind.Mutable getMind(JSONObject p) {
        return minds.get(p.getString("mind"));
    }

    private Idea.Mutable getIdea(JSONObject p, String key) {
        return getMind(p).find(p.getString(key));
    }

    private Idea.Mutable getIdea(JSONObject p) {
        return getIdea(p, "idea");
    }

    Method mind = (p) -> {
        final String name = p.getString("name");
        if (!minds.containsKey(name)) {
            minds.put(name, new MutableMindImpl());
        }
        return respond(NO_ERROR);
    };

    Method imagine = (p) -> {
        getMind(p).imagine(p.getString("idea"));
        return respond(NO_ERROR);
    };

    Method become = (p) -> {
        getIdea(p).become(getIdea(p, "intension"));
        return respond(NO_ERROR);
    };

    Method possess = (p) -> {
        getIdea(p).possess(p.getString("name"), getIdea(p, "type"), getMind(p).makeValueGenerator(p.getJSONObject("defaultValue")));
        return respond(NO_ERROR);
    };

    Method reify = (p) -> {
        getIdea(p).reify(p.getString("name"), getIdea(p, "type"), getMind(p).valueOf(p.getJSONObject("value")));
        return respond(NO_ERROR);
    };

    int INSTANTIATION_FAILED = defineError(126, "Instantiation failed");

    Method instantiate = (p) -> {
        final Idea.Mutable i = getIdea(p);
        if (p.has("serialization")) {
            if (i.instantiate(p.getJSONObject("serialization")) == null) {
                return respond(INSTANTIATION_FAILED);
            }
        } else {
            i.instantiate();
        }
        return respond(NO_ERROR);
    };
}
