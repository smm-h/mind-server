package ir.smmh.tgbot;

import ir.smmh.api.StandardAPI;
import org.json.JSONObject;

public interface StandardAPIBot extends APIBot {
    @Override
    StandardAPI getAPI();

    default JSONObject processRequest(long chatId, String request) {
        return getAPI().processJSON(request);
    }

    @Override
    default void process(long chatId, String text, int messageId) {
        final JSONObject response = processRequest(chatId, text);
        if (response.has("results")) {
            sendMessage(chatId, processResults(response.getJSONObject("results")), messageId);
        } else if (response.has("error_message")) {
            sendMessage(chatId, response.getString("error_message"), messageId);
        } else {
            sendMessage(chatId, response.getString("description"), messageId);
        }
    }

    default String processResults(JSONObject results) {
        return "<pre>" + results.toString(2) + "</pre>";
    }
}
