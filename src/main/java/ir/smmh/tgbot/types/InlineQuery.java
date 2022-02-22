package ir.smmh.tgbot.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InlineQuery extends UpdateContent {

    /**
     * @return Unique identifier for this query
     */
    @NotNull String id();

    /**
     * @return Sender
     */
    @NotNull User from();

    /**
     * @return Text of the query (up to 256 characters)
     */
    @NotNull String query();

    /**
     * @return Offset of the results to be returned, can be
     * controlled by the bot
     */
    @NotNull String offset();

    /**
     * @return Type of the chat, from which the inline query was
     * sent. Can be either `sender` for a private chat with the
     * inline query sender, `private`, `group`, `supergroup`, or
     * `channel`. The chat type should be always known for requests
     * sent from official clients and most third-party clients,
     * unless the request was sent from a secret chat
     */
    @Nullable String chat_type();

    /**
     * @return Sender location, only for bots that request user
     * location
     */
    @Nullable Location location();

}
