package vsdl.whost.connections;

import vsdl.datavector.api.DataMessageHandler;
import vsdl.datavector.link.DataLink;

import java.net.Socket;
import java.util.ArrayList;

public class ConnectionManager {

    private final ArrayList<Connection> CONNECTIONS;
    private final DataMessageHandler HANDLER;

    public ConnectionManager(DataMessageHandler dmh) {
        CONNECTIONS = new ArrayList<>();
        HANDLER = dmh;
    }

    public void connectOnSocket(Socket s) {
        CONNECTIONS.add(new Connection(new DataLink(s, HANDLER)));
    }

    public void terminateConnection(long sessionID) {
        CONNECTIONS.removeIf(c -> c.getId() == sessionID);
    }
}
