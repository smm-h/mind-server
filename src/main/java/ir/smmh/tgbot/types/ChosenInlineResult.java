package ir.smmh.tgbot.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a result of an inline query that was chosen by the user
 * and sent to their chat partner. It is necessary to enable inline
 * feedback via @BotFather in order to receive these objects in updates.
 */
public interface ChosenInlineResult extends UpdateContent {
    /**
     * The unique identifier for the result that was chosen
     */
    @NotNull String result_id();

    /**
     * The user that chose the result
     */
    @NotNull User from();

    /**
     * Sender location, only for bots that require user location
     */
    @Nullable Location location();

    /**
     * Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message. Will be also received in callback queries and can be used to edit the message.
     */
    @Nullable String inline_message_id();

    /**
     * The query that was used to obtain the result
     */
    @NotNull String query();
}
