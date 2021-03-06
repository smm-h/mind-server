package ir.smmh.tgbot.types;

import ir.smmh.util.ReadOnlyJSON;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InlineQueryResult extends ReadOnlyJSON {
    @NotNull String type();

    /**
     * @return Unique identifier for this result, 1-64 Bytes
     */
    @NotNull String id();

//        /**
//         * Inline keyboard attached to the message
//         */
//         @Nullable InlineKeyboardMarkup reply_markup();
//
//        /**
//         * Content of the message to be sent, instead of the actual media/message
//         * This field is required if InlineQueryResult.Video is used to send an
//         * HTML-page as a result (e.g., a YouTube video).
//         */
//         @Nullable InputMessageContent input_message_content();

    /**
     * Represents a link to an article or web page.
     */
    interface Article extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "article";
        }

        /**
         * Title of the result
         */
        @NotNull String title();

        /**
         * URL of the result
         */
        @Nullable String url();

        /**
         * Pass True, if you don't want the URL to be shown in the message
         */
        @Nullable Boolean hide_url();

        /**
         * Short description of the result
         */
        @Nullable String description();

        /**
         * Url of the thumbnail for the result
         */
        @Nullable String thumb_url();

        /**
         * Thumbnail width
         */
        @Nullable Integer thumb_width();

        /**
         * Thumbnail height
         */
        @Nullable Integer thumb_height();
    }

    /**
     * Represents a link to a photo. By default, this photo will be sent by
     * the user with optional caption. Alternatively, you can use
     * input_message_content to send a message with the specified content
     * instead of the photo.
     */
    interface Photo extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "photo";
        }

        /**
         * A valid URL of the photo. Photo must be in JPEG format.
         * Photo size must not exceed 5MB
         */
        @NotNull String photo_url();

        /**
         * URL of the thumbnail for the photo
         */
        @NotNull String thumb_url();

        /**
         * Width of the photo
         */
        @Nullable Integer photo_width();

        /**
         * Height of the photo
         */
        @Nullable Integer photo_height();

        /**
         * Title for the result
         */
        @Nullable String title();

        /**
         * Short description of the result
         */
        @Nullable String description();

        /**
         * Caption of the photo to be sent, 0-1024 characters after entities parsing
         */
        @Nullable String caption();
    }

    /**
     * Represents a link to an animated GIF file. By default, this animated
     * GIF file will be sent by the user with optional caption. Alternatively,
     * you can use input_message_content to send a message with the specified
     * content instead of the animation.
     */
    interface Gif extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "gif";
        }

        /**
         * A valid URL for the GIF file. File size must not exceed 1MB
         */
        @NotNull String gif_url();

        /**
         * Width of the GIF
         */
        @Nullable Integer gif_width();

        /**
         * Height of the GIF
         */
        @Nullable Integer gif_height();

        /**
         * Duration of the GIF in seconds
         */
        @Nullable Integer gif_duration();

        /**
         * URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for
         * the result
         */
        @NotNull String thumb_url();

        /**
         * MIME type of the thumbnail, must be one of `image/jpeg`,
         * `image/gif`, or `video/mp4`. Defaults to `image/jpeg`
         */
        @Nullable String thumb_mime_type();

        /**
         * Title for the result
         */
        @Nullable String title();

        /**
         * Caption of the GIF file to be sent, 0-1024 characters after entities parsing
         */
        @Nullable String caption();
    }

    /**
     * Represents a link to a video animation (H.264/MPEG-4 AVC video without
     * sound). By default, this animated MPEG-4 file will be sent by the user
     * with optional caption. Alternatively, you can use input_message_content
     * to send a message with the specified content instead of the animation.
     */
    interface Mpeg4Gif extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "mpeg4_gif";
        }

        /**
         * A valid URL for the MP4 file. File size must not exceed 1MB
         */
        @NotNull String mpeg4_url();

        /**
         * Video width
         */
        @Nullable Integer mpeg4_width();

        /**
         * Video height
         */
        @Nullable Integer mpeg4_height();

        /**
         * Video duration in seconds
         */
        @Nullable Integer mpeg4_duration();

        /**
         * URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for
         * the result
         */
        @NotNull String thumb_url();

        /**
         * MIME type of the thumbnail, must be one of `image/jpeg`,
         * `image/gif`, or `video/mp4`. Defaults to `image/jpeg`
         */
        @Nullable String thumb_mime_type();

        /**
         * Title for the result
         */
        @Nullable String title();

        /**
         * Caption of the MPEG-4 file to be sent, 0-1024 characters after
         * entities parsing
         */
        @Nullable String caption();
    }

    /**
     * Represents a link to a page containing an embedded video player or a
     * video file. By default, this video file will be sent by the user with
     * an optional caption. Alternatively, you can use input_message_content
     * to send a message with the specified content instead of the video.
     */
    interface Video extends InlineQueryResult {

        // If an InlineQueryResultVideo message contains an embedded video
        // (e.g., YouTube), you must replace its content using
        // input_message_content.

        @Override
        @NotNull
        default String type() {
            return "video";
        }

        /**
         * A valid URL for the embedded video player or video file
         */
        @NotNull String video_url();

        /**
         * Mime type of the content of video url, `text/html` or `video/mp4`
         */
        @NotNull String mime_type();

        /**
         * URL of the thumbnail (JPEG only) for the video
         */
        @NotNull String thumb_url();

        /**
         * Title for the result
         */
        @NotNull String title();

        /**
         * Caption of the video to be sent, 0-1024 characters after entities
         * parsing
         */
        @Nullable String caption();

        /**
         * Video width
         */
        @Nullable Integer video_width();

        /**
         * Video height
         */
        @Nullable Integer video_height();

        /**
         * Video duration in seconds
         */
        @Nullable Integer video_duration();

        /**
         * Short description of the result
         */
        @Nullable String description();
    }

    /**
     * Represents a link to an MP3 audio file. By default, this audio file
     * will be sent by the user. Alternatively, you can use
     * input_message_content to send a message with the specified content
     * instead of the audio.
     */
    interface Audio extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "audio";
        }

        /**
         * A valid URL for the audio file
         */
        @NotNull String audio_url();

        /**
         * Title
         */
        @NotNull String title();

        /**
         * Caption, 0-1024 characters after entities parsing
         */
        @Nullable String caption();

        /**
         * Performer
         */
        @Nullable String performer();

        /**
         * Audio duration in seconds
         */
        @Nullable Integer audio_duration();

    }

    /**
     * Represents a link to a voice recording in an .OGG container encoded
     * with OPUS. By default, this voice recording will be sent by the user.
     * Alternatively, you can use input_message_content to send a message
     * with the specified content instead of the the voice message.
     */
    interface Voice extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "voice";
        }

        /**
         * A valid URL for the voice recording
         */
        @NotNull String voice_url();

        /**
         * Recording title
         */
        @NotNull String title();

        /**
         * Caption, 0-1024 characters after entities parsing
         */
        @Nullable String caption();

        /**
         * Recording duration in seconds
         */
        @Nullable Integer voice_duration();

    }

    /**
     * Represents a link to a file. By default, this file will be sent by
     * the user with an optional caption. Alternatively, you can use
     * input_message_content to send a message with the specified content
     * instead of the file. Currently, only .PDF and .ZIP files can be sent
     * using this method.
     */
    interface Document extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "document";
        }

        /**
         * Title for the result
         */
        @NotNull String title();

        /**
         * Caption of the document to be sent, 0-1024 characters after
         * entities parsing
         */
        @Nullable String caption();

        /**
         * A valid URL for the file
         */
        @NotNull String document_url();

        /**
         * Mime type of the content of the file, either `application/pdf` or
         * `application/zip`
         */
        @NotNull String mime_type();

        /**
         * Short description of the result
         */
        @Nullable String description();

        /**
         * URL of the thumbnail (JPEG only) for the file
         */
        @Nullable String thumb_url();

        /**
         * Thumbnail width
         */
        @Nullable Integer thumb_width();

        /**
         * Thumbnail height
         */
        @Nullable Integer thumb_height();

    }

    /**
     * Represents a location on a map. By default, the location will be sent
     * by the user. Alternatively, you can use input_message_content to send
     * a message with the specified content instead of the location.
     */
    interface Location extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "location";
        }

        /**
         * Location latitude in degrees
         */
        float latitude();

        /**
         * Location longitude in degrees
         */
        float longitude();

        /**
         * Location title
         */
        @NotNull String title();

        /**
         * The radius of uncertainty for the location, measured in meters;
         * 0-1500
         */
        @Nullable Float horizontal_accuracy();

        /**
         * Period in seconds for which the location can be updated,
         * should be between 60 and 86400.
         */
        @Nullable Integer live_period();

        /**
         * For live locations, a direction in which the user is moving,
         * in degrees. Must be between 1 and 360 if specified.
         */
        @Nullable Integer heading();

        /**
         * For live locations, a maximum distance for proximity alerts about
         * approaching another chat member, in meters. Must be between
         * 1 and 100000 if specified.
         */
        @Nullable Integer proximity_alert_radius();

        /**
         * Url of the thumbnail for the result
         */
        @Nullable String thumb_url();

        /**
         * Thumbnail width
         */
        @Nullable Integer thumb_width();

        /**
         * Thumbnail height
         */
        @Nullable Integer thumb_height();

    }

    /**
     * Represents a venue. By default, the venue will be sent by the user.
     * Alternatively, you can use input_message_content to send a message
     * with the specified content instead of the venue.
     */
    interface Venue extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "venue";
        }

        /**
         * Latitude of the venue location in degrees
         */
        float latitude();

        /**
         * Longitude of the venue location in degrees
         */
        float longitude();

        /**
         * Title of the venue
         */
        @NotNull String title();

        /**
         * Address of the venue
         */
        @NotNull String address();

        /**
         * Foursquare identifier of the venue if known
         */
        @Nullable String foursquare_id();

        /**
         * Foursquare type of the venue, if known. (Consult documentation)
         */
        @Nullable String foursquare_type();

        /**
         * Google Places identifier of the venue
         */
        @Nullable String google_place_id();

        /**
         * Google Places type of the venue. (Consult documentation)
         */
        @Nullable String google_place_type();

        /**
         * Url of the thumbnail for the result
         */
        @Nullable String thumb_url();

        /**
         * Thumbnail width
         */
        @Nullable Integer thumb_width();

        /**
         * Thumbnail height
         */
        @Nullable Integer thumb_height();

    }

    /**
     * Represents a contact with a phone number. By default, this contact
     * will be sent by the user. Alternatively, you can use input_message_content
     * to send a message with the specified content instead of the contact.
     */
    interface Contact extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "contact";
        }

        /**
         * Contact's phone number
         */
        @NotNull String phone_number();

        /**
         * Contact's first name
         */
        @NotNull String first_name();

        /**
         * Contact's last name
         */
        @Nullable String last_name();

        /**
         * Additional data about the contact in the form of a vCard, 0-2048 bytes
         */
        @Nullable String vcard();

        /**
         * Url of the thumbnail for the result
         */
        @Nullable String thumb_url();

        /**
         * Thumbnail width
         */
        @Nullable Integer thumb_width();

        /**
         * Thumbnail height
         */
        @Nullable Integer thumb_height();

    }

    /**
     * Represents a Game.
     */
    interface Game extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "game";
        }

        /**
         * Short name of the game
         */
        @NotNull String game_short_name();

    }

    /**
     * Represents a link to a photo stored on the Telegram servers.
     * By default, this photo will be sent by the user with an optional caption.
     * Alternatively, you can use input_message_content to send a message with
     * the specified content instead of the photo.
     */
    interface CachedPhoto extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "photo";
        }

        /**
         * A valid file identifier of the photo
         */
        @NotNull String photo_file_id();

        /**
         * Title for the result
         */
        @Nullable String title();

        /**
         * Short description of the result
         */
        @Nullable String description();

        /**
         * Caption of the photo to be sent, 0-1024 characters after entities parsing
         */
        @Nullable String caption();
    }

    /**
     * Represents a link to an animated GIF file stored on the Telegram servers.
     * By default, this animated GIF file will be sent by the user with an
     * optional caption. Alternatively, you can use input_message_content to
     * send a message with specified content instead of the animation.
     */
    interface CachedGif extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "gif";
        }

        /**
         * A valid file identifier for the GIF file
         */
        @NotNull String gif_file_id();

        /**
         * Title for the result
         */
        @Nullable String title();

        /**
         * Caption of the GIF file to be sent, 0-1024 characters after entities parsing
         */
        @Nullable String caption();
    }

    /**
     * Represents a link to a video animation (H.264/MPEG-4 AVC video without
     * sound) stored on the Telegram servers. By default, this animated MPEG-4
     * file will be sent by the user with an optional caption. Alternatively,
     * you can use input_message_content to send a message with the specified
     * content instead of the animation.
     */
    interface CachedMpeg4Gif extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "mpeg4_gif";
        }

        /**
         * A valid file identifier for the MP4 file
         */
        @NotNull String mpeg4_file_id();

        /**
         * Title for the result
         */
        @Nullable String title();

        /**
         * Caption of the MPEG-4 file to be sent, 0-1024 characters after entities parsing
         */
        @Nullable String caption();
    }

    /**
     * Represents a link to a sticker stored on the Telegram servers.
     * By default, this sticker will be sent by the user. Alternatively, you
     * can use input_message_content to send a message with the specified
     * content instead of the sticker.
     */
    interface CachedSticker extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "sticker";
        }

        /**
         * A valid file identifier of the sticker
         */
        @NotNull String sticker_file_id();

    }

    /**
     * Represents a link to a file stored on the Telegram servers. By default,
     * this file will be sent by the user with an optional caption.
     * Alternatively, you can use input_message_content to send a message
     * with the specified content instead of the file.
     */
    interface CachedDocument extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "document";
        }

        /**
         * Title for the result
         */
        @NotNull String title();

        /**
         * A valid file identifier for the file
         */
        @NotNull String document_file_id();

        /**
         * Short description of the result
         */
        @Nullable String description();

        /**
         * Caption of the document to be sent, 0-1024 characters after entities parsing
         */
        @Nullable String caption();

    }

    /**
     * Represents a link to a video file stored on the Telegram servers.
     * By default, this video file will be sent by the user with an optional
     * caption. Alternatively, you can use input_message_content to send a
     * message with the specified content instead of the video.
     */
    interface CachedVideo extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "video";
        }

        /**
         * A valid file identifier for the video file
         */
        @NotNull String video_file_id();

        /**
         * Title for the result
         */
        @NotNull String title();

        /**
         * Short description of the result
         */
        @Nullable String description();

        /**
         * Caption of the video to be sent, 0-1024 characters after entities parsing
         */
        @Nullable String caption();
    }

    /**
     * Represents a link to a voice message stored on the Telegram servers.
     * By default, this voice message will be sent by the user. Alternatively,
     * you can use input_message_content to send a message with the specified
     * content instead of the voice message.
     */
    interface CachedVoice extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "voice";
        }

        /**
         * A valid file identifier for the voice message
         */
        @NotNull String voice_file_id();

        /**
         * Voice message title
         */
        @NotNull String title();

        /**
         * Caption, 0-1024 characters after entities parsing
         */
        @Nullable String caption();

    }

    /**
     * Represents a link to an MP3 audio file stored on the Telegram servers.
     * By default, this audio file will be sent by the user. Alternatively,
     * you can use input_message_content to send a message with the specified
     * content instead of the audio.
     */
    interface CachedAudio extends InlineQueryResult {

        @Override
        @NotNull
        default String type() {
            return "audio";
        }

        /**
         * A valid file identifier for the audio file
         */
        @NotNull String audio_file_id();

        /**
         * Caption, 0-1024 characters after entities parsing
         */
        @Nullable String caption();
    }
}
