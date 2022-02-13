package ir.smmh.apps.cmdbot;

import ir.smmh.api.API;
import ir.smmh.net.server.impl.ServerImpl;
import ir.smmh.util.Map;
import ir.smmh.util.impl.MapImpl;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.*;
import java.util.StringJoiner;

@ParametersAreNonnullByDefault
@SuppressWarnings("SpellCheckingInspection")
public class CmdAPI implements API {

    private final Map.SingleValue.Mutable<String, Cmd> cmds = new MapImpl.SingleValue.Mutable<>();
    private Cmd cmd;

    public CmdAPI() {
        try {
            String defaultName = "default";
            cmd = new Cmd(defaultName);
            cmds.setAtPlace(defaultName, cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        API api = new CmdAPI();
//        new APIBotImpl(api).start("Bots.r5bot");
        new ServerImpl(api).start(7000);
    }

    @Override
    public @NotNull String process(String text) {
        if (text.charAt(0) == '/') {
            String[] r = text.split(" ");
            //noinspection SwitchStatementWithTooFewBranches
            switch (r[0]) {
                case "/p":
                    String name = r[1];
                    if (cmds.containsPlace(name)) {
                        cmd = cmds.getAtPlace(name);
                        return "Switched to existing process";
                    } else {
                        try {
                            cmd = new Cmd(name);
                            cmds.setAtPlace(name, cmd);
                            return "Switched to a new process";
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return "Could not switch process";
            }
        }
        try {
//            cmd.process.getOutputStream().write(text.getBytes(StandardCharsets.UTF_8));
            cmd.writer.write(text + "\n");
        } catch (IOException e) {
            return "I/O error while writing to the stream:\n" + e.getMessage();
        }
        try {
            cmd.process.waitFor();
        } catch (InterruptedException e) {
            return "Process was interrupted:\n" + e.getMessage();
        }
        StringJoiner joiner = new StringJoiner("\n");
        while (true) {
            try {
                String line = cmd.reader.readLine();
                if (line == null) break;
                joiner.add(line);
            } catch (IOException e) {
                return "I/O error while reading from the stream:\n" + e.getMessage();
            }
        }
        return joiner.toString();
    }

    private static class Cmd {
        final String name;
        final Process process;
        final BufferedReader reader;
        final BufferedWriter writer;

        Cmd(String name) throws IOException {
            this.name = name;
            process = new ProcessBuilder("cmd", "/c").redirectErrorStream(true).start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        }
    }
}
