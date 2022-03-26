package ir.smmh.mpg.ttt;

import ir.smmh.mpg.lobby.Player;
import ir.smmh.mpg.lobby.impl.RoomImpl;
import ir.smmh.net.api.Method;

import java.io.IOException;
import java.util.Arrays;

import static ir.smmh.util.JSONUtil.map;

public class TTTGame extends RoomImpl {

    private static final int[][] WINS = {
            {0, 1, 2}, {3, 4, 5},
            {6, 7, 8}, {0, 3, 6},
            {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };
    private final int[] cells = new int[9];
    private final Player[] players = new Player[2];
    private GameState gameState = GameState.NOT_STARTED;
    private int turn0 = 1;
    private int turn = 0;

    public TTTGame(Player creator) throws IOException {
        super(creator);
        Arrays.fill(cells, -1);
        defineError(WRONG_NUMBER_OF_PLAYERS, "Wrong number of players");
        defineMethod("is_it_my_turn", (Method.Authenticated<Player>) (player, parameters) -> ok(map("is_it_your_turn", player == players[turn])));
        defineMethod("play", (Method.Authenticated<Player>) (player, parameters) -> {

            if (gameState != GameState.PLAYING)
                return notOk("Game not in progress");

            if (player != players[turn])
                return notOk("It is not your turn");

            int position = parameters.getInt("position");
            System.out.println(player.getUsername() + " played " + position);
            if (position < 0 || position >= cells.length)
                return notOk("Invalid position");

            cells[position] = turn;
            if (checkIfWon(turn)) {
                gameState = GameState.OVER;
                System.out.println("Game over! winner is: " + player.getUsername());
            } else if (checkIfTied()) {
                gameState = GameState.TIED;
                System.out.println("Game tied!");
            } else {
                turn = (turn + 1) % 2;
            }

            broadcast(map(
                    "update", "player_played",
                    "player_id", player.getUsername(),
                    "played", position
            ).toString());

            return ok();
        });
    }

    @Override
    public int leave(Player player) {
//        turn = (turn + 1) % 2;
        gameState = GameState.OVER;
        return super.leave(player);
    }

    @Override
    public int start() {
        if (gameState == GameState.PLAYING) {
            return GAME_IN_PROGRESS;
        } else {
            lock();
            if (getSize() != 2) {
                unlock();
                return WRONG_NUMBER_OF_PLAYERS;
            }
            var p = getMembers();
            for (int i = 0; i < 2; i++) {
                players[i] = p.getAtIndex(i).getKey();
            }
            turn0 = 1 - turn0;
            turn = turn0;
            Arrays.fill(cells, -1);
            gameState = GameState.PLAYING;
            return NO_ERROR;
        }
    }

    private boolean checkIfWon(int i) {
        for (int[] win : WINS)
            if (cells[win[0]] == i && cells[win[1]] == i && cells[win[2]] == i)
                return true;
        return false;
    }

    private boolean checkIfTied() {
        for (int cell : cells)
            if (cell == -1)
                return false;
        return true;
    }

    private enum GameState {
        NOT_STARTED, PLAYING, OVER, TIED
    }
}
