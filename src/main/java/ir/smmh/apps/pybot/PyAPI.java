package ir.smmh.apps.pybot;

import ir.smmh.net.api.API;
import ir.smmh.net.server.impl.ServerImpl;
import org.jetbrains.annotations.NotNull;
import org.python.util.PythonInterpreter;

import java.io.IOException;

import static ir.smmh.util.FunctionalUtil.with;

public class PyAPI implements API {
    private final PythonInterpreter interpreter = new PythonInterpreter();

    public static void main(String[] args) throws IOException {
        API api = new PyAPI();
//        new APITelegramBotImpl(api, null).start(TOKEN);
        new ServerImpl(api, 7000).start();
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
