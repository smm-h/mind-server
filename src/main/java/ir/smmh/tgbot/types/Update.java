package ir.smmh.tgbot.types;

import ir.smmh.tgbot.types.impl.ChosenInlineResultImpl;
import ir.smmh.tgbot.types.impl.InlineQuery;
import ir.smmh.tgbot.types.impl.Message;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

@FunctionalInterface
public
interface Update<C extends UpdateContent> {
    @NotNull C getContent();

    interface Handler<C extends UpdateContent> {
        void handle(C content);

        @NotNull C create(JSONObject object);

        default void handle(JSONObject object) {
            handle(create(object));
        }

        @NotNull String allowedUpdateType();

        interface MessageHandler extends Handler<ir.smmh.tgbot.types.Message> {
            @Override
            default @NotNull ir.smmh.tgbot.types.Message create(JSONObject object) {
                return new Message(object);
            }
        }

        /**
         * New incoming message of any kind — text, photo, sticker, etc.
         */
        @FunctionalInterface
        interface message extends Handler.MessageHandler {
            @Override
            default @NotNull String allowedUpdateType() {
                return "message";
            }
        }

        /**
         * New version of a message that is known to the bot and was edited
         */
        @FunctionalInterface
        interface edited_message extends Handler.MessageHandler {
            @Override
            default @NotNull String allowedUpdateType() {
                return "edited_message";
            }
        }

        /**
         * New incoming channel post of any kind — text, photo, sticker, etc.
         */
        @FunctionalInterface
        interface channel_post extends Handler.MessageHandler {
            @Override
            default @NotNull String allowedUpdateType() {
                return "channel_post";
            }
        }

        /**
         * New version of a channel post that is known to the bot and was edited
         */
        @FunctionalInterface
        interface edited_channel_post extends Handler.MessageHandler {
            @Override
            default @NotNull String allowedUpdateType() {
                return "edited_channel_post";
            }
        }

        /**
         * New incoming inline query
         */
        @FunctionalInterface
        interface inline_query extends Handler<ir.smmh.tgbot.types.InlineQuery> {
            @Override
            default @NotNull String allowedUpdateType() {
                return "inline_query";
            }

            @Override
            default @NotNull ir.smmh.tgbot.types.InlineQuery create(JSONObject object) {
                return new InlineQuery(object);
            }
        }

        /**
         * The result of an inline query that was chosen by a user and sent
         * to their chat partner. Please see our documentation on the feedback
         * collecting for details on how to enable these updates for your bot.
         */
        @FunctionalInterface
        interface chosen_inline_result extends Handler<ChosenInlineResult> {
            @Override
            default @NotNull String allowedUpdateType() {
                return "chosen_inline_result";
            }

            @Override
            default @NotNull ChosenInlineResult create(JSONObject object) {
                return new ChosenInlineResultImpl(object);
            }
        }

        /**
         * New incoming callback query
         */
        @FunctionalInterface
        interface callback_query extends Handler<CallbackQuery> {
            @Override
            default @NotNull String allowedUpdateType() {
                return "callback_query";
            }

            @Override
            default @NotNull CallbackQuery create(JSONObject object) {
                return new ir.smmh.tgbot.types.impl.CallbackQuery(object);
            }
        }

        /**
         * New incoming shipping query. Only for invoices with flexible price
         */
        @FunctionalInterface
        interface shipping_query extends Handler<ShippingQuery> {
            @Override
            default @NotNull String allowedUpdateType() {
                return "shipping_query";
            }

            @Override
            default @NotNull ShippingQuery create(JSONObject object) {
                return new ir.smmh.tgbot.types.impl.ShippingQuery(object);
            }
        }

        /**
         * New incoming pre-checkout query. Contains full information about
         * checkout
         */
        @FunctionalInterface
        interface pre_checkout_query extends Handler<PreCheckoutQuery> {
            @Override
            default @NotNull String allowedUpdateType() {
                return "pre_checkout_query";
            }

            @Override
            default @NotNull PreCheckoutQuery create(JSONObject object) {
                return new ir.smmh.tgbot.types.impl.PreCheckoutQuery(object);
            }
        }

        /**
         * New poll state. Bots receive only updates about stopped polls and
         * polls, which are sent by the bot
         */
        @FunctionalInterface
        interface poll extends Handler<Poll> {
            @Override
            default @NotNull String allowedUpdateType() {
                return "poll";
            }

            @Override
            default @NotNull Poll create(JSONObject object) {
                return new ir.smmh.tgbot.types.impl.Poll(object);
            }
        }

        /**
         * A user changed their answer in a non-anonymous poll. Bots receive
         * new votes only in polls that were sent by the bot itself.
         */
        @FunctionalInterface
        interface poll_answer extends Handler<PollAnswer> {
            @Override
            default @NotNull String allowedUpdateType() {
                return "poll_answer";
            }

            @Override
            default @NotNull PollAnswer create(JSONObject object) {
                return new ir.smmh.tgbot.types.impl.PollAnswer(object);
            }
        }

        /**
         * The bot's chat member status was updated in a chat. For private chats,
         * this update is received only when the bot is blocked or unblocked
         * by the user.
         */
        @FunctionalInterface
        interface my_chat_member extends Handler<ChatMemberUpdated> {
            @Override
            default @NotNull String allowedUpdateType() {
                return "my_chat_member";
            }

            @Override
            default @NotNull ChatMemberUpdated create(JSONObject object) {
                return new ir.smmh.tgbot.types.impl.ChatMemberUpdated(object);
            }
        }

        /**
         * A chat member's status was updated in a chat. The bot must be an
         * administrator in the chat and must explicitly specify `chat_member`
         * in the list of allowed_updates to receive these updates.
         */
        @FunctionalInterface
        interface chat_member extends Handler<ChatMemberUpdated> {
            @Override
            default @NotNull String allowedUpdateType() {
                return "chat_member";
            }

            @Override
            default @NotNull ChatMemberUpdated create(JSONObject object) {
                return new ir.smmh.tgbot.types.impl.ChatMemberUpdated(object);
            }
        }

        /**
         * A request to join the chat has been sent. The bot must have the
         * can_invite_users administrator right in the chat to receive these
         * updates.
         */
        @FunctionalInterface
        interface chat_join_request extends Handler<ChatJoinRequest> {
            @Override
            default @NotNull String allowedUpdateType() {
                return "chat_join_request";
            }

            @Override
            default @NotNull ChatJoinRequest create(JSONObject object) {
                return new ir.smmh.tgbot.types.impl.ChatJoinRequest(object);
            }
        }
    }

}
