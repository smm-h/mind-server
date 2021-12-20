package ir.smmh.mind.api;

import ir.smmh.net.StandardAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ir.smmh.mind.api.Methods.*;

public class MindsAPI extends StandardAPI<Method> {
    @Override
    public @Nullable Method findMethod(@NotNull String methodName) {
        switch (methodName) {
            case "imagine":
                return imagine;
            case "become":
                return become;
            case "possess":
                return possess;
            case "reify":
                return reify;
            case "instantiate":
                return instantiate;
            default:
                return null;
        }
    }
}
