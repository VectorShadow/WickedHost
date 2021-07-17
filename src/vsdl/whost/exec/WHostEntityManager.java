package vsdl.whost.exec;

import vsdl.datavector.link.LinkSessionManager;
import vsdl.whost.connections.ConnectionListenerDaemon;
import vsdl.whost.connections.WHostDataMessageHandler;

public class WHostEntityManager {

    private static ConnectionListenerDaemon connectionListenerDaemon = null;
    private static LinkSessionManager linkSessionManager = null;

    public static ConnectionListenerDaemon getConnectionListenerDaemon() {
        if (connectionListenerDaemon == null) {
            connectionListenerDaemon = new ConnectionListenerDaemon();
        }
        return connectionListenerDaemon;
    }

    public static LinkSessionManager getLinkSessionManager() {
        if (linkSessionManager == null) {
            linkSessionManager = new LinkSessionManager(new WHostDataMessageHandler());
        }
        return linkSessionManager;
    }
}
