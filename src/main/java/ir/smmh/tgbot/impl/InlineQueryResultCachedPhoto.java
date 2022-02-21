package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.TelegramBot.InlineQueryResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class InlineQueryResultCachedPhoto extends InlineQueryResultImpl implements InlineQueryResult.CachedPhoto {
    private final @NotNull String photo_file_id;
    private final @Nullable String title, description, caption;

    public InlineQueryResultCachedPhoto(String id, String photo_file_id, @Nullable String title, @Nullable String description, @Nullable String caption) {
        super(new JSONObject()
                .put("photo_file_id", photo_file_id)
                .put("title", title)
                .put("description", description)
                .put("caption", caption), id);
        this.photo_file_id = photo_file_id;
        this.title = title;
        this.description = description;
        this.caption = caption;
    }

    @Override
    public @NotNull String photo_file_id() {
        return photo_file_id;
    }

    @Override
    public @Nullable String title() {
        return title;
    }

    @Override
    public @Nullable String description() {
        return description;
    }

    @Override
    public @Nullable String caption() {
        return caption;
    }
}
