package ir.smmh.apps.pybot;

import ir.smmh.api.API;
import ir.smmh.tgbot.impl.APITelegramBotImpl;
import org.jetbrains.annotations.NotNull;
import org.python.util.PythonInterpreter;

import static ir.smmh.util.FunctionalUtil.with;

public class PyAPI implements API {
    private final PythonInterpreter interpreter = new PythonInterpreter();

    public static void main(String[] args) {
        API api = new PyAPI();
        new APITelegramBotImpl(api, null).start("Bots.r5bot");
//        new ServerImpl(api).start(7000);
    }

    @Override
    public @NotNull String process(@NotNull String request) {
        try {
            return interpreter.eval(request).toString();
        } catch (Throwable e) {
            return "Error: " + with(e.getMessage(), e.toString());
        }
    }
}
