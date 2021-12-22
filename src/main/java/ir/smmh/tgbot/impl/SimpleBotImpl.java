package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.SimpleBot;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class SimpleBotImpl implements SimpleBot {

    private static final String BASE = "https://api.telegram.org/bot%s/%s";

    private final HttpClient client;
    private String token;
    private int updateId = 0;
    private boolean running = false;

    public SimpleBotImpl() {
        client = HttpClient.newHttpClient();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void sendMessage(long chatId, String text, @Nullable Integer replyToMessageId) {
        final JSONObject p = new JSONObject();
        p.put("chat_id", chatId);
        p.put("text", text);
        p.put("parse_mode", "HTML");
        if (replyToMessageId != null) {
            p.put("reply_to_message_id", replyToMessageId);
            p.put("allow_sending_without_reply", true);
        }
        client.sendAsync(HttpRequest.newBuilder()
                .uri(makeURI("sendMessage"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(p.toString()))
                .build(), HttpResponse.BodyHandlers.ofString()
        );
    }

    private URI makeURI(String method) {
        return URI.create(String.format(BASE, token, method));
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
                client.sendAsync(HttpRequest.newBuilder()
                        .uri(makeURI("getUpdates"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(String.format(params, updateId)))
                        .build(), HttpResponse.BodyHandlers.ofString()
                )
                        .thenApply(HttpResponse::body)
                        .thenApply(s -> new JSONObject(new JSONTokener(s)))
                        .thenAccept(this::handle)
                        .join();
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
    }
}
