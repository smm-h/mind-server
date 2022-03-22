package ir.smmh.net.server.impl;

import ir.smmh.net.api.StandardAPI;
import ir.smmh.net.server.Client;
import ir.smmh.net.server.StandardServer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StandardServerImpl<C extends Client> extends ServerImpl implements StandardServer<C> {
    private final Set<C> connections = Collections.synchronizedSet(new HashSet<>());

    public StandardServerImpl(StandardAPI api) {
        super(api);
    }

    @Override
    public void addConnection(C connection) {
        connections.add(connection);
    }

    @Override
    public int getConnectionCount() {
        return connections.size();
    }

    @Override
    public void forEach(ConnectionAction<C> action) {
        // iteration over synchronized collections must be within
        // manually synchronized blocks as per its documentation
        int failures = 0;
        synchronized (connections) {
            for (C connection : connections) {
                if (connection.isClosed()) {
                    connections.remove(connection);
                } else {
                    try {
                        action.accept(connection);
                    } catch (IOException e) {
                        failures++;
                    }
                }
            }
        }
        if (failures > 0)
            System.err.println("Action failed for " + failures + " connections(s)");
    }

    @Override
    public void broadcast(String data) {
        forEach(connection -> connection.send(data));
    }
}
