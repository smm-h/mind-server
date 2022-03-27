package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.UpdateContent;
import ir.smmh.util.impl.ReadOnlyJSONImpl;
import org.json.JSONObject;

public class ContentImpl extends ReadOnlyJSONImpl implements UpdateContent {

    public ContentImpl(JSONObject wrapper) {
        super(wrapper);
    }

}
