package ir.smmh.tgbot;

import ir.smmh.tgbot.impl.ChatImpl;
import ir.smmh.tgbot.impl.ContentImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.function.Predicate;

public interface TelegramBot {

//    TelegramBotMarkupWriter getMarkupWriter();

    Predicate<TelegramBot> RUNNING = TelegramBot::isRunning;

    void start(String withToken);

    void stop();

    boolean isRunning();

    void addHandler(Update.Handler<?> handler);

    @NotNull User.Myself getMe() throws MethodFailedException;

    void sendMessage(long chatId, String text, @Nullable Integer replyToMessageId);

    void sendPhoto(long chatId, File file, String caption, @Nullable Integer replyToMessageId) throws FileNotFoundException;

    interface User {
        long id();

        boolean is_bot();

        @NotNull String first_name();

        @Nullable String last_name();

        @Nullable String username();

        @Nullable String language_code();

        interface Myself extends User {
            @Override
            default boolean is_bot() {
                return true;
            }

            boolean can_join_groups();

            boolean can_read_all_group_messages();

            boolean supports_inline_queries();
        }
    }

    interface Chat {

        static Chat of(JSONObject object) {
            return ChatImpl.of(object);
        }

        long id();

        interface Private extends Chat {
            @Nullable String username();

            @Nullable String first_name();

            @Nullable String last_name();
        }

        interface Group extends Chat {
            @NotNull String title();
        }

        interface Supergroup extends Chat {
            @NotNull String title();

            @Nullable String username();
        }

        interface Channel extends Chat {
            @NotNull String title();

            @Nullable String username();
        }
    }

    @FunctionalInterface
    interface Update<C extends Update.Content> {
        @NotNull C getContent();

        interface Handler<C extends Update.Content> {
            void handle(C content);

            @NotNull C create(JSONObject object);

            default void handle(JSONObject object) {
                handle(create(object));
            }

            @NotNull String allowedUpdateType();

            interface MessageHandler extends Handler<Content.Message> {
                @Override
                default @NotNull Content.Message create(JSONObject object) {
                    return new ContentImpl.Message(object);
                }
            }

            @FunctionalInterface
            interface message extends MessageHandler {
                @Override
                default @NotNull String allowedUpdateType() {
                    return "message";
                }
            }

            @FunctionalInterface
            interface edited_message extends MessageHandler {
                @Override
                default @NotNull String allowedUpdateType() {
                    return "edited_message";
                }
            }

            @FunctionalInterface
            interface inline_query extends Handler<Content.InlineQuery> {
                @Override
                default @NotNull String allowedUpdateType() {
                    return "inline_query";
                }

                @Override
                default @NotNull Content.InlineQuery create(JSONObject object) {
                    return new ContentImpl.InlineQuery(object);
                }
            }
        }

        interface Content {
            interface Message extends Content {
                int message_id();

                @Nullable String text();

                @Nullable User from();

                @NotNull Chat chat();
            }

            interface InlineQuery extends Content {
                int id();

                @NotNull String query();

                @NotNull User from();
            }
        }
    }
}
