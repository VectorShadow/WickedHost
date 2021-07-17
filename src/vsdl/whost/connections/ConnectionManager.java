package vsdl.whost.connections;

import vsdl.datavector.api.DataMessageHandler;
import vsdl.datavector.crypto.CryptoUtilities;
import vsdl.datavector.crypto.RSA;
import vsdl.datavector.elements.DataMessage;
import vsdl.datavector.elements.DataMessageBuilder;
import vsdl.datavector.link.DataLink;
import vsdl.wl.elements.DataMessageType;

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
        DataLink dl = new DataLink(s, HANDLER);
        CONNECTIONS.add(new Connection(dl));
        dl.transmit(
                DataMessageBuilder
                        .start(DataMessageType.PUBLIC_KEY.name())
                        .addBlock(RSA.getSessionPublicKey().toString(Character.MAX_RADIX))
                        .build()
        );
    }

    public void terminateConnection(long sessionID) {
        CONNECTIONS.removeIf(c -> c.getId() == sessionID);
    }
}
