package ir.smmh.tgbot.types.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tgbot.types.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class PollAnswer extends ContentImpl implements ir.smmh.tgbot.types.PollAnswer {
    public PollAnswer(JSONObject wrapped) {
        super(wrapped);
    }

    @Override
    public @NotNull String poll_id() {
        return getString("poll_id");
    }

    @Override
    public @NotNull User user() {
        return User.of(getJSONObject("user"));
    }

    @Override
    public @NotNull Sequential<Integer> option_ids() {
        return getSequential("option_ids");
    }
}
