package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.SimpleBot;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.NetworkUtil;
import okhttp3.*;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class SimpleBotImpl implements SimpleBot {

    private static final String BASE = "https://api.telegram.org/bot%s/%s";
    private final OkHttpClient client;
    private String token;
    private int updateId;
    private boolean running;

    public SimpleBotImpl() {
        super();
        client = new OkHttpClient();
    }

    @Override
    public final boolean isRunning() {
        return running;
    }

    @Override
    public final void sendMessage(long chatId, String text, @Nullable Integer replyToMessageId) {
        JSONObject p = new JSONObject();
        try {
            p.put("chat_id", chatId);
            p.put("text", text);
            p.put("parse_mode", "HTML");
            /*
             * <b>bold</b>, <strong>bold</strong>
             * <i>italic</i>, <em>italic</em>
             * <u>underline</u>, <ins>underline</ins>
             * <s>strikethrough</s>, <strike>strikethrough</strike>, <del>strikethrough</del>
             * <span class="tg-spoiler">spoiler</span>, <tg-spoiler>spoiler</tg-spoiler>
             * <b>bold <i>italic bold <s>italic bold strikethrough <span class="tg-spoiler">italic bold strikethrough spoiler</span></s> <u>underline italic bold</u></i> bold</b>
             * <a href="http://www.example.com/">inline URL</a>
             * <a href="tg://user?id=123456789">inline mention of a user</a>
             * <code>inline fixed-width code</code>
             * <pre>pre-formatted fixed-width code block</pre>
             * <pre><code class="language-python">pre-formatted fixed-width code block written in the Python programming language</code></pre>
             */
            if (replyToMessageId != null) {
                p.put("reply_to_message_id", replyToMessageId);
                p.put("allow_sending_without_reply", true);
            }
            RequestBody body = RequestBody.create(p.toString(), NetworkUtil.JSON);
            Request request = new Request.Builder()
                    .url(makeURL("sendMessage"))
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();
            client.newCall(request).execute();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void sendPhoto(long chatId, File file, @Nullable String caption, @Nullable Integer replyToMessageId) throws FileNotFoundException {
        if (!file.exists()) throw new FileNotFoundException();
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("chat_id", String.valueOf(chatId))
                    .addFormDataPart("photo", file.getName(), RequestBody.create(file, MediaType.parse("image/png")));
            if (caption != null) {
                builder.addFormDataPart("caption", caption);
                builder.addFormDataPart("parse_mode", "HTML");
            }
            if (replyToMessageId != null) {
                builder.addFormDataPart("reply_to_message_id", String.valueOf(replyToMessageId));
                builder.addFormDataPart("allow_sending_without_reply", String.valueOf(true));
            }
            Request request = new Request.Builder()
                    .url(makeURL("sendPhoto"))
                    .post(builder.build())
                    .build();
            client.newCall(request).execute();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String makeURL(String method) {
        return String.format(BASE, token, method);
    }

    @Override
    public final void stop() {
        running = false;
    }

    @Override
    public final void start(String withToken) {

        System.out.println("Bot started...");

        if (running)
            stop();

        running = true;
        token = withToken;

        final String params = "{\"timeout\": 3, \"allowed_updates\": [\"message\"], \"offset\": %d}";

        while (running) {
            try {

                RequestBody requestBody = RequestBody.create(String.format(params, updateId), NetworkUtil.JSON);
                Request request = new Request.Builder().url(makeURL("getUpdates")).addHeader("Content-Type", "application/json").post(requestBody).build();
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                handle(JSONUtil.parse(responseBody == null ? "{}" : responseBody.string()));
            } catch (JSONException | IOException e) {
                System.err.println(e.getMessage());
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
            JSONArray array = object.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                JSONObject update = array.getJSONObject(i);
                updateId = Math.max(updateId, update.getInt("update_id") + 1);
                if (update.has("message")) {
                    JSONObject message = update.getJSONObject("message");
                    int messageId = message.getInt("message_id");
                    if (message.has("text")) {
                        String text = message.getString("text");
                        long chatId = message.getJSONObject("chat").getLong("id");
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
