package ir.smmh.util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public interface Serializable {
    @NotNull String serialize() throws SerializationException;

    interface JSON extends Serializable {
        @NotNull JSONObject serializeJSON() throws JSONException;

        @Override
        @NotNull
        default String serialize() throws SerializationException {
            try {
                return serializeJSON().toString();
            } catch (JSONException e) {
                throw new SerializationException(e);
            }
        }
    }

    class SerializationException extends Exception {

        public SerializationException() {
            super();
        }

        public SerializationException(String message) {
            super(message);
        }

        public SerializationException(Throwable throwable) {
            super(throwable);
        }
    }
}
