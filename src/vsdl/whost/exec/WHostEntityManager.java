package vsdl.whost.exec;

import vsdl.whost.connections.ConnectionListenerDaemon;
import vsdl.whost.connections.ConnectionManager;
import vsdl.whost.connections.WHostDataMessageHandler;

public class WHostEntityManager {

    private static ConnectionListenerDaemon CONNECTION_LISTENER_DAEMON = null;
    private static ConnectionManager CONNECTION_MGR = null;

    public static ConnectionListenerDaemon getConnectionListenerDaemon() {
        if (CONNECTION_LISTENER_DAEMON == null) {
            CONNECTION_LISTENER_DAEMON = new ConnectionListenerDaemon();
        }
        return CONNECTION_LISTENER_DAEMON;
    }

    public static ConnectionManager getConnectionManager() {
        if (CONNECTION_MGR == null) {
            CONNECTION_MGR = new ConnectionManager(new WHostDataMessageHandler());
        }
        return CONNECTION_MGR;
    }
}
