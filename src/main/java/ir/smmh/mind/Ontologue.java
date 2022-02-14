package ir.smmh.mind;

import ir.smmh.tgbot.impl.StandardAPIBotImpl;
import org.json.JSONObject;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

@SuppressWarnings("SpellCheckingInspection")
@ParametersAreNonnullByDefault
public class Ontologue extends StandardAPIBotImpl {

    final String mindName = "shared";

    public Ontologue() {
        super(new MindsAPI());
        JSONObject parameters = new JSONObject();
        parameters.put("name", mindName);
        process("mind", parameters);
    }

    @Override
    public final JSONObject processRequest(long chatId, String request) {
        String[] s = request.split(" ");
        switch (s[0].toLowerCase(Locale.ROOT)) {
            case "/whoami": {
                // TODO
                break;
            }
            case "/everyone": {
                // TODO
                break;
            }
            case "/everything": {
                // TODO
                break;
            }
            case "/every": {
                // TODO
                break;
            }
            case "/createmind": {
                // TODO
                break;
            }
            case "/iam": {
                // TODO
                break;
            }
            case "/imagine": {
                JSONObject parameters = new JSONObject();
                parameters.put("mind", mindName);
                parameters.put("name", s[1]);
                return process("imagine", parameters);
            }
            case "/whatis": {
                JSONObject parameters = new JSONObject();
                parameters.put("mind", mindName);
                parameters.put("idea", s[1]);
                return process("idea", parameters);
            }
            case "/is": {
                JSONObject parameters = new JSONObject();
                parameters.put("mind", mindName);
                parameters.put("idea", s[1]);
                parameters.put("intension", s[2]);
                return process("is", parameters);
            }
            case "/has": {
                JSONObject parameters = new JSONObject();
                parameters.put("mind", mindName);
                parameters.put("idea", s[1]);
                parameters.put("name", s[2]);
                return process("has", parameters);
            }
            case "/become": {
                JSONObject parameters = new JSONObject();
                parameters.put("mind", mindName);
                parameters.put("idea", s[1]);
                parameters.put("intension", s[2]);
                return process("become", parameters);
            }
            case "/possess": {
                JSONObject parameters = new JSONObject();
                parameters.put("mind", mindName);
                parameters.put("idea", s[1]);
                parameters.put("name", s[2]);
                parameters.put("type", s[3]);
                parameters.put("defaultValue", s[4]);
                return process("possess", parameters);
            }
            case "/reify": {
                // TODO
                break;
            }
            case "/instantiate": {
                // TODO
                break;
            }
        }
        return getAPI().respond(getAPI().METHOD_NOT_FOUND);
    }

    final JSONObject process(String method, JSONObject parameters) {
        JSONObject request = new JSONObject();
        request.put("method", method);
        request.put("parameters", parameters);
        return getAPI().processJSON(request.toString());
    }

    @Override
    public final String processResults(JSONObject results) {
        if (results.has("code")) {
            return "<pre>" + results.getString("code") + "</pre>";
        } else if (results.has("is")) {
            return "<pre>" + results.getBoolean("is") + "</pre>";
        } else if (results.has("has")) {
            return "<pre>" + results.getBoolean("has") + "</pre>";
        } else {
            return super.processResults(results);
        }
    }
}
