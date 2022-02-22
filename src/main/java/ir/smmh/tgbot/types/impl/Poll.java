package ir.smmh.tgbot.types.impl;

import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class Poll extends ContentImpl implements ir.smmh.tgbot.types.Poll {
    public Poll(JSONObject wrapped) {
        super(wrapped);
    }

    @Override
    public @NotNull String id() {
        return getString("id");
    }

    @Override
    public @NotNull String question() {
        return getString("question");
    }

    @Override
    public @NotNull Sequential<JSONObject> options() {
        return getSequential("options");
    }

    @Override
    public int total_voter_count() {
        return 0;
    }

    @Override
    public boolean is_closed() {
        return false;
    }

    @Override
    public boolean is_anonymous() {
        return false;
    }

    @Override
    public @NotNull String type() {
        return getString("type");
    }

    @Override
    public boolean allows_multiple_answers() {
        return false;
    }

    @Override
    public @Nullable Integer correct_option_id() {
        return has("correct_option_id") ? getInt("correct_option_id") : null;
    }

    @Override
    public @Nullable String explanation() {
        return getNullableString("explanation");
    }

    @Override
    public @Nullable Sequential<JSONObject> explanation_entities() {
        return getNullableSequential("explanation_entities");
    }

    @Override
    public @Nullable Integer open_period() {
        return has("open_period") ? getInt("open_period") : null;
    }

    @Override
    public @Nullable Integer close_date() {
        return has("close_date") ? getInt("close_date") : null;
    }
}
