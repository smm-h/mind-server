package ir.smmh.mpg.lobby;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface GameStarter {
    @Nullable Game startNew(JSONObject gameSettings);
}
