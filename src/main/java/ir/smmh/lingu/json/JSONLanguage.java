package ir.smmh.lingu.json;

import ir.smmh.lingu.Code;
import ir.smmh.lingu.Maker;
import ir.smmh.lingu.impl.LanguageImpl;
import ir.smmh.lingu.processors.impl.MultiprocessorImpl;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONLanguage extends LanguageImpl implements Maker<JSONObject> {

    public JSONLanguage() {
        super("JSON", "json", new MultiprocessorImpl());
    }

    @Override
    public @NotNull JSONObject makeFromCode(@NotNull Code code) throws MakingException {
        try {
            return new JSONObject(new JSONTokener(code.getOpenFile().read()));
        } catch (JSONException e) {
            throw new MakingException(e);
        }
    }
}
