package ir.smmh.tgbot.types;

import ir.smmh.tgbot.types.impl.ChatInviteLinkImpl;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface ChatInviteLink extends JSONUtil.ReadOnlyJSON {
    @Contract("!null->!null")
    static ChatInviteLink of(@Nullable JSONObject wrapped) {
        return ChatInviteLinkImpl.of(wrapped);
    }
    // TODO attributes of ChatInviteLink
}
