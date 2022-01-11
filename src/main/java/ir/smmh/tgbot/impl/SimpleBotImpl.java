package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.SimpleBot;
import ir.smmh.util.JSONUtil;
import okhttp3.*;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public abstract class SimpleBotImpl implements SimpleBot {

    private static final String BASE = "https://api.telegram.org/bot%s/%s";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client;
    private String token;
    private int updateId = 0;
    private boolean running = false;

    public SimpleBotImpl() {
        client = new OkHttpClient();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void sendMessage(long chatId, String text, @Nullable Integer replyToMessageId) {
        final JSONObject p = new JSONObject();
        try {
            p.put("chat_id", chatId);
            p.put("text", text);
            p.put("parse_mode", "HTML");
            if (replyToMessageId != null) {
                p.put("reply_to_message_id", replyToMessageId);
                p.put("allow_sending_without_reply", true);
            }
            RequestBody body = RequestBody.create(p.toString(), JSON);
            Request request = new Request.Builder().url(makeURL("sendMessage")).addHeader("Content-Type", "application/json").post(body).build();
            client.newCall(request).execute();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String makeURL(String method) {
        return String.format(BASE, token, method);
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public void start(String token) {

        if (isRunning())
            stop();

        running = true;
        this.token = token;

        final String params = "{\"timeout\": 3, \"allowed_updates\": [\"message\"], \"offset\": %d}";

        while (running) {
            try {

                RequestBody body = RequestBody.create(String.format(params, updateId), JSON);
                Request request = new Request.Builder().url(makeURL("getUpdates")).addHeader("Content-Type", "application/json").post(body).build();
                Response response = client.newCall(request).execute();
                handle(JSONUtil.parse(response.body().string()));
            } catch (Throwable throwable) {
                System.err.println(throwable.getMessage());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(JSONObject object) {
        try {
            final JSONArray array = object.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                final JSONObject update = array.getJSONObject(i);
                updateId = Math.max(updateId, update.getInt("update_id") + 1);
                if (update.has("message")) {
                    final JSONObject message = update.getJSONObject("message");
                    final int messageId = message.getInt("message_id");
                    if (message.has("text")) {
                        final String text = message.getString("text");
                        final long chatId = message.getJSONObject("chat").getLong("id");
                        System.out.println("@" + chatId + " #" + messageId + ": " + text);
                        process(chatId, text, messageId);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
