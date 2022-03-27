package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.InlineQueryResult;
import ir.smmh.util.impl.ReadOnlyJSONImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class InlineQueryResultImpl extends ReadOnlyJSONImpl implements InlineQueryResult {

    private final @NotNull String id;

    protected InlineQueryResultImpl(@NotNull JSONObject wrapped, @NotNull String id) {
        super(wrapped.put("id", id));
        this.id = id;
    }

    @Override
    public @NotNull String type() {
        return getString("type");
    }

    @Override
    public @NotNull String id() {
        return id;
    }

    public static class CachedPhoto extends InlineQueryResultImpl implements InlineQueryResult.CachedPhoto {
        private final @NotNull String photo_file_id;
        private final @Nullable String title, description, caption;

        public CachedPhoto(String id, String photo_file_id, @Nullable String title, @Nullable String description, @Nullable String caption) {
            super(new JSONObject()
                    .put("type", "photo")
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
}
