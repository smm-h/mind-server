package ir.smmh.tgbot;

import ir.smmh.api.JSONAPIImpl;
import org.json.JSONObject;

public interface StandardAPITelegramBot extends APITelegramBot {
    @Override
    JSONAPIImpl getAPI();

    default JSONObject processRequest(long chatId, String request) {
        return getAPI().processJSON(request);
    }

    @Override
    default void process(long chatId, String text, int messageId) {
        String string;
        JSONObject response = processRequest(chatId, text);
        if (response.has("results")) {
            string = processResults(response.getJSONObject("results"));
        } else if (response.has("error_message")) {
            string = response.getString("error_message");
        } else {
            string = response.getString("description");
        }
        sendMessage(chatId, string, messageId);
    }

    default String processResults(JSONObject results) {
        return "<pre>" + results.toString(2) + "</pre>";
    }
}
