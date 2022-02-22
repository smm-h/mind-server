package ir.smmh.tgbot.types;

import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.NotNull;

/**
 * This object represents an answer of a user in a non-anonymous poll.
 */
public interface PollAnswer extends UpdateContent {
    /**
     * Unique poll identifier
     */
    @NotNull String poll_id();

    /**
     * The user, who changed the answer to the poll
     */
    @NotNull User user();

    /**
     * 0-based identifiers of answer options, chosen by the user.
     * May be empty if the user retracted their vote.
     */
    @NotNull Sequential<Integer> option_ids();
}
