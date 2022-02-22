package ir.smmh.tgbot.types;

import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * This object contains information about a poll.
 */
public interface Poll extends UpdateContent {
    /**
     * Unique poll identifier
     */
    @NotNull String id();

    /**
     * Poll question, 1-300 characters
     */
    @NotNull String question();

    /**
     * List of poll options
     */
    @NotNull Sequential<JSONObject> options(); // TODO PollOption

    /**
     * Total number of users that voted in the poll
     */
    int total_voter_count();

    /**
     * True, if the poll is closed
     */
    boolean is_closed();

    /**
     * True, if the poll is anonymous
     */
    boolean is_anonymous();

    /**
     * Poll type, currently can be `regular` or `quiz`
     */
    @NotNull String type();

    /**
     * True, if the poll allows multiple answers
     */
    boolean allows_multiple_answers();

    /**
     * 0-based identifier of the correct answer option. Available only
     * for polls in the quiz mode, which are closed, or was sent (not
     * forwarded) by the bot or to the private chat with the bot.
     */
    @Nullable Integer correct_option_id();

    /**
     * Text that is shown when a user chooses an incorrect answer or
     * taps on the lamp icon in a quiz-style poll, 0-200 characters
     */
    @Nullable String explanation();

    /**
     * Special entities like usernames, URLs, bot commands, etc. that
     * appear in the explanation
     */
    @Nullable Sequential<JSONObject> explanation_entities(); // TODO MessageEntity

    /**
     * Amount of time in seconds the poll will be active after creation
     */
    @Nullable Integer open_period();

    /**
     * Point in time (Unix timestamp) when the poll will be automatically closed
     */
    @Nullable Integer close_date();
}
