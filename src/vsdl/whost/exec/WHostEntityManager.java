package vsdl.whost.exec;

import vsdl.datavector.link.LinkSessionManager;
import vsdl.whost.connections.ConnectionListenerDaemon;
import vsdl.whost.connections.WHostDataMessageHandler;
import vsdl.whost.data.provider.DataProvider;

public class WHostEntityManager {

    private static ConnectionListenerDaemon connectionListenerDaemon = null;
    private static DataProvider dataProvider = null;
    private static LinkSessionManager linkSessionManager = null;

    public static ConnectionListenerDaemon getConnectionListenerDaemon() {
        if (connectionListenerDaemon == null) {
            connectionListenerDaemon = new ConnectionListenerDaemon();
        }
        return connectionListenerDaemon;
    }

    public static DataProvider getDataProvider() {
        if (dataProvider == null) {
            dataProvider = new DataProvider();
        }
        return dataProvider;
    }

    public static LinkSessionManager getLinkSessionManager() {
        if (linkSessionManager == null) {
            linkSessionManager = new LinkSessionManager(new WHostDataMessageHandler());
        }
        return linkSessionManager;
    }
}
