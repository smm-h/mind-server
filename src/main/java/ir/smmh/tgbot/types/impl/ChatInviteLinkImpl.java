package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.ChatInviteLink;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class ChatInviteLinkImpl extends JSONUtil.ReadOnlyJSONImpl implements ChatInviteLink {


    public ChatInviteLinkImpl(JSONObject wrapped) {
        super(wrapped);
    }

    @Contract("!null->!null")
    public static ChatInviteLink of(@Nullable JSONObject wrapped) {
        if (wrapped == null) return null;
        return new ChatInviteLinkImpl(wrapped);
    }
}
