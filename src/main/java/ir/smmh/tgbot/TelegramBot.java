package ir.smmh.tgbot;

import ir.smmh.tgbot.impl.ChatImpl;
import ir.smmh.tgbot.impl.ContentImpl;
import ir.smmh.tgbot.impl.UserImpl;
import org.jetbrains.annotations.Contract;
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

        @Contract("!null->!null")
        static User of(@Nullable JSONObject wrapped) {
            return UserImpl.of(wrapped);
        }

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

        @Contract("!null->!null")
        static Chat of(@Nullable JSONObject wrapped) {
            return ChatImpl.of(wrapped);
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

    interface Location {
        float longitude();

        float latitude();

        /**
         * @return The radius of uncertainty for the location, measured in
         * meters; 0-1500
         */
        @Nullable Float horizontal_accuracy();

        interface Live extends Location {
            /**
             * @return Time relative to the message sending date, during which
             * the location can be updated; in seconds. For active live
             * locations only.
             */
            @Nullable Integer live_period();

            /**
             * @return The direction in which user is moving, in degrees; 1-360.
             * For active live locations only.
             */
            @Nullable Integer heading();

            /**
             * @return Maximum distance for proximity alerts about approaching
             * another chat member, in meters. For sent live locations only.
             */
            @Nullable Integer proximity_alert_radius();
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
