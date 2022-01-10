package ir.smmh.lingu.settings.err;

import ir.smmh.lingu.impl.MishapImpl;
import ir.smmh.lingu.settings.FormalSettings;
import ir.smmh.lingu.settings.Length;

public class InvalidLength extends MishapImpl.Caused {

    private final String key;
    private final Length validLength;

    public InvalidLength(FormalSettings src, String key) {
        super(src.getTokenAt(key, 2), true);
        this.key = key;
        this.validLength = src.getValidLengthOf(key);
    }

    @Override
    public String getReport() {
        return "length of `" + key + "` must be `" + validLength + "`";
    }
}
