package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.MethodFailedException;
import ir.smmh.tgbot.TelegramBot;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.NetworkUtil;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public abstract class TelegramBotImpl implements TelegramBot {

    private final String BASE = "https://api.telegram.org/bot%s/%s";
    private final OkHttpClient client;
    private final @Nullable String parseMode;
    private final int maxTries = 3;
    private final List<Update.Handler<?>> handlers = new ArrayList<>();
    private String params;
    private String token;
    private int updateId;
    private boolean running;

    public TelegramBotImpl(@Nullable String parseMode) {
        super();
        this.parseMode = parseMode;
        client = new OkHttpClient();
    }

    @Override
    public void addHandler(Update.Handler<?> handler) {
        handlers.add(handler);
        JSONArray array = new JSONArray();
        for (Update.Handler<?> handlers : handlers) {
            array.put(handlers.allowedUpdateType());
        }
        params = "{\"timeout\": 3, \"allowed_updates\": " + array + ", \"offset\": %d}";
    }

    @Override
    public final boolean isRunning() {
        return running;
    }

    @Override
    public @NotNull User.Myself getMe() throws MethodFailedException {
        try {
            Request request = new Request.Builder()
                    .url(makeURL("getMe"))
//                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            response.close();
            if (body == null) throw new MethodFailedException();
            return UserImpl.myself(JSONUtil.parse(body.string()));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            throw new MethodFailedException();
        }
    }

    @Override
    public final void sendMessage(long chatId, String text, @Nullable Integer replyToMessageId) {
        JSONObject p = new JSONObject();
//        System.out.println(text);
        try {
            p.put("chat_id", chatId);
            p.put("text", text);
            if (parseMode != null) {
                p.put("parse_mode", parseMode);
            }
            if (replyToMessageId != null) {
                p.put("reply_to_message_id", replyToMessageId);
                p.put("allow_sending_without_reply", "true");
            }
            RequestBody body = RequestBody.create(p.toString(), NetworkUtil.JSON);
            Request request = new Request.Builder()
                    .url(makeURL("sendMessage"))
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
//            System.out.println(response.body().string());
            response.close();
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
                if (parseMode != null) {
                    builder.addFormDataPart("parse_mode", parseMode);
                }
            }
            if (replyToMessageId != null) {
                builder.addFormDataPart("reply_to_message_id", String.valueOf(replyToMessageId));
                builder.addFormDataPart("allow_sending_without_reply", "true");
            }
            int tries = 0;
            while (true) {
                try {
                    client.newCall(new Request.Builder()
                            .url(makeURL("sendPhoto"))
                            .post(builder.build())
                            .build()
                    )
                            .execute()
                            .close();
                    break;
                } catch (SocketTimeoutException e) {
                    if (tries++ < maxTries) {
                        System.err.println("SOCKET TIMED OUT; TRYING AGAIN");
                    } else {
                        System.err.println("SOCKET TIMED OUT; NOT TRYING AGAIN");
                        break;
                    }
                }
            }
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

        while (running) {
            try {
                RequestBody requestBody = RequestBody.create(String.format(params, updateId), NetworkUtil.JSON);
                Request request = new Request.Builder().url(makeURL("getUpdates")).addHeader("Content-Type", "application/json").post(requestBody).build();
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    try {
                        JSONArray array = JSONUtil.parse(responseBody.string()).getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject update = array.getJSONObject(i);
                            updateId = Math.max(updateId, update.getInt("update_id") + 1);
                            for (Update.Handler<?> handler : handlers) {
                                String allowedUpdate = handler.allowedUpdateType();
                                if (update.has(allowedUpdate)) {
                                    handler.handle(update.getJSONObject(allowedUpdate));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    responseBody.close();
                } else {
                    System.err.println("Null response body");
                }
            } catch (JSONException | IOException e) {
                System.err.println(e.getMessage());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("LONG POLLING INTERRUPTED");
            }
        }
    }
}
