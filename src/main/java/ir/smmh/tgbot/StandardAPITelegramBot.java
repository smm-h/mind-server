package ir.smmh.tgbot;

import ir.smmh.api.JSONAPIImpl;
import org.json.JSONObject;

public interface StandardAPITelegramBot extends APITelegramBot {
    @Override
    JSONAPIImpl getAPI();

    default void handleViaStandardAPI(Update.Content.Message message) {
        String text = message.text();
        if (text == null) return;
        long chatId = message.chat().id();
        String string;
        JSONObject response = getAPI().processJSON(text);
        if (response.has("results")) {
            string = processResults(response.getJSONObject("results"));
        } else if (response.has("error_message")) {
            string = response.getString("error_message");
        } else {
            string = response.getString("description");
        }
        sendMessage(chatId, string, message.message_id());
    }

    default String processResults(JSONObject results) {
        return "<pre>" + results.toString(2) + "</pre>";
    }
}
