package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.UpdateContent;
import ir.smmh.util.JSONUtil;
import org.json.JSONObject;

public class ContentImpl extends JSONUtil.ReadOnlyJSONImpl implements UpdateContent {

    public ContentImpl(JSONObject wrapper) {
        super(wrapper);
    }

}
