package vsdl.whost.exec;

import vsdl.datavector.link.LinkSessionManager;
import vsdl.whost.connections.ConnectionListenerDaemon;
import vsdl.whost.connections.WHostDataMessageHandler;
import vsdl.wrepo.cql.DatabaseManager;

public class WHostEntityManager {

    private static ConnectionListenerDaemon connectionListenerDaemon = null;
    private static DatabaseManager databaseManager = null;
    private static LinkSessionManager linkSessionManager = null;

    public static ConnectionListenerDaemon getConnectionListenerDaemon() {
        if (connectionListenerDaemon == null) {
            connectionListenerDaemon = new ConnectionListenerDaemon();
        }
        return connectionListenerDaemon;
    }

    public static DatabaseManager getDatabaseManager() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
            databaseManager.startup();
        }
        return databaseManager;
    }

    public static LinkSessionManager getLinkSessionManager() {
        if (linkSessionManager == null) {
            linkSessionManager = new LinkSessionManager(new WHostDataMessageHandler());
        }
        return linkSessionManager;
    }

    public static void shutdown() {
        connectionListenerDaemon.kill();
        databaseManager.shutdown();
    }
}
