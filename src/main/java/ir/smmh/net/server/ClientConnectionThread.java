package ir.smmh.net.server;

public interface ClientConnectionThread {
    /**
     * When you use this method, the connection will no longer be automatically
     * closed because it is assumed that you handle that.
     *
     * @return The connection with a client
     */
    ClientConnection getConnection();
}
