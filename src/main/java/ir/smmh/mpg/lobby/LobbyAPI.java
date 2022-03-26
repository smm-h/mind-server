package ir.smmh.mpg.lobby;


import ir.smmh.mpg.lobby.impl.PlayerImpl;
import ir.smmh.mpg.lobby.impl.SessionImpl;
import ir.smmh.mpg.ttt.DesktopClient;
import ir.smmh.mpg.ttt.TTTGame;
import ir.smmh.net.api.AuthenticatorImpl;
import ir.smmh.net.api.Method;
import ir.smmh.net.api.StandardAPIImpl;
import ir.smmh.net.client.Client;
import ir.smmh.net.client.impl.StandardClientImpl;
import ir.smmh.net.server.ClientConnectionThread;
import ir.smmh.net.server.Server;
import ir.smmh.net.server.impl.StandardServerImpl;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.Map;
import ir.smmh.util.RandomUtil;
import ir.smmh.util.impl.MapImpl;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import static ir.smmh.util.JSONUtil.map;

public class LobbyAPI extends StandardAPIImpl {

    private static LobbyAPI instance;
    private final Map.SingleValue.Mutable<String, RoomBuilder> games = new MapImpl.SingleValue.Mutable<>();
    private final Map.SingleValue.Mutable<String, Room> rooms = new MapImpl.SingleValue.Mutable<>();

    private LobbyAPI() {
        super(new AuthenticatorImpl<Player, Session>() {
            @Override
            public @NotNull Session createSession(Player user, String token, String sessionId) {
                return new SessionImpl(user, token, sessionId);
            }

            @Override
            public @NotNull Player createUser(String username, String passwordHash) {
                return new PlayerImpl(username, passwordHash);
            }

            @Override
            public boolean isPasswordStrongEnough(String username) {
                return true;
            }

            @Override
            public boolean isUsernameInvalid(String username) {
                return false;
            }

            @Override
            public boolean isSessionStronger(Session current, Session other) {
                return true;
            }
        });
        games.setAtPlace("ttt", TTTGame::new);
        defineMethod("create_room", (Method.Authenticated<Player>) (player, parameters) -> {

            if (rooms.getSize() >= 100)
                return notOk("Server too busy");

            // find the room builder
            String gameName = parameters.getString("game_name");
            RoomBuilder roomBuilder = games.getAtPlace(gameName);
            if (roomBuilder == null)
                return notOk("No such game");

            // build a new room
            Room room;
            try {
                room = roomBuilder.build(player);
            } catch (IOException e) {
                System.err.println("I/O error while building room");
                return errorCode(BUG);
            }

            // return room id
            int tries = 0;
            String roomId = null;
            while (roomId == null || rooms.containsPlace(roomId)) {
                roomId = RandomUtil.generateRandomHex(8);
                tries++;
                if (tries >= 3)
                    return notOk("Please try again later");
            }
            rooms.setAtPlace(roomId, room);
            return ok("room_id", roomId);
        });
        defineMethod("enter_room", (Method.Authenticated<Player>) (player, parameters) -> {

            // find and join a room
            String roomId = parameters.getString("room_id");
            Room room = rooms.getAtPlace(roomId);
            if (room == null)
                return notOk("No such room");

            return errorCode(room.enter(player, ((ClientConnectionThread) Thread.currentThread()).getConnection()));
        });
        defineMethod("get_room_port", (Method.Authenticated<Player>) (player, parameters) -> {

            // find the room
            String roomId = parameters.getString("room_id");
            Room room = rooms.getAtPlace(roomId);
            if (room == null)
                return notOk("No such room");

            return ok("room_port", room.getPort());
        });
        defineMethod("start_game", (Method.Authenticated<Player>) (player, parameters) -> {

            // find the room
            String roomId = parameters.getString("room_id");
            Room room = rooms.getAtPlace(roomId);
            if (room == null)
                return notOk("No such room");

            room.start();
            return ok();
        });
    }

    public static void main(String[] args) throws IOException {
        int port = 7000;
        String hostAddress = "localhost";
        Server server = new StandardServerImpl(LobbyAPI.getInstance(), port);
        new Thread(server::start).start();
        new Thread(() -> {
            Client c1 = new StandardClientImpl(port, hostAddress);
            JSONObject auth1 = signUpAndSignIn(c1, "p1");
            String roomId = methodicalRequest(c1, "create_room", auth1, map("game_name", "ttt")).getJSONObject("results").getString("room_id");
            methodicalRequest(c1, "enter_room", auth1, map("room_id", roomId));

            Client c2 = new StandardClientImpl(port, hostAddress);
            JSONObject auth2 = signUpAndSignIn(c2, "p2");
            methodicalRequest(c2, "enter_room", auth2, map("room_id", roomId));

            int roomPort = methodicalRequest(c1, "get_room_port", auth1, map("room_id", roomId)).getJSONObject("results").getInt("room_port");

            methodicalRequest(c1, "start_game", auth1, map("room_id", roomId));

            new Thread(() -> new DesktopClient(roomPort, hostAddress, auth1)).start();
            new Thread(() -> new DesktopClient(roomPort, hostAddress, auth2)).start();

//            boolean turn = false;
//            try (Scanner scanner = new Scanner(System.in)) {
//                while (true) {
//                    int position = scanner.nextInt();
//                    if (methodicalRequest(turn ? c1 : c2, "play", turn ? auth1 : auth2, map("position", position)).getBoolean("ok")) {
//                        turn = !turn;
//                    }
//                }
//            }

        }).start();
    }

    private static JSONObject signUpAndSignIn(Client client, String shorthand) {
        client.sendRequest("{\"method\": \"sign_up\", \"parameters\": {\"username\": \"" + shorthand + "\", \"password\": \"" + shorthand + "\"}}");
        client.sendRequest("{\"method\": \"sign_in\", \"parameters\": {\"username\": \"" + shorthand + "\", \"password\": \"" + shorthand + "\", \"token\": \"" + shorthand + "\"}}");
        return map("username", shorthand, "token", shorthand);
    }

    private static JSONObject methodicalRequest(Client client, String method, JSONObject auth, JSONObject params) {
        return JSONUtil.parse(client.sendRequest(new JSONObject()
                .put("method", method)
                .put("authentication", auth)
                .put("parameters", params)
                .toString()));
    }

    public static LobbyAPI getInstance() {
        return instance == null ? (instance = new LobbyAPI()) : instance;
    }

    public interface RoomBuilder {
        Room build(Player creator) throws IOException;
    }
}
