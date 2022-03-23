package ir.smmh.mpg.lobby;

public interface Game {
    /**
     * Tries to add a player to the list of players in a game
     * @param player Player to add
     * @return Error code
     */
    int join(Player player);
}
