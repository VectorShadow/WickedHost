package vsdl.whost.exec;

import vsdl.datavector.crypto.CryptoUtilities;
import vsdl.datavector.link.LinkSessionManager;
import vsdl.whost.connections.ConnectionListenerDaemon;
import vsdl.whost.connections.handlers.WHostDataMessageHandler;
import vsdl.whost.crypto.SecurityConstants;
import vsdl.wrepo.cql.DatabaseManager;
import vsdl.wrepo.cql.query.QueryType;

import static vsdl.whost.crypto.SecurityConstants.generateSalt;
import static vsdl.wrepo.cql.query.Database.ROOT_USR;

public class WHostEntityManager {

    private static ConnectionListenerDaemon connectionListenerDaemon = null;
    private static DatabaseManager databaseManager = null;
    private static LinkSessionManager linkSessionManager = null;

    public static ConnectionListenerDaemon getConnectionListenerDaemon() {
        if (connectionListenerDaemon == null) {
            connectionListenerDaemon = new ConnectionListenerDaemon();
            connectionListenerDaemon.start();
        }
        return connectionListenerDaemon;
    }

    public static DatabaseManager getDatabaseManager() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
            databaseManager.startup();
            //create the root account if not exists
            if (databaseManager.query(QueryType.LOGON_USER_EXISTS, ROOT_USR).get(0) == null) {
                String rootPassword = CryptoUtilities.randomAlphaNumericString(SecurityConstants.SALT_LENGTH);
                System.out.println("Root user did not exist - creating...");
                String salt = generateSalt();
                String hashedRootPassword = CryptoUtilities.hash(CryptoUtilities.salt(rootPassword, salt));
                databaseManager.query(QueryType.LOGON_CREATE_USER, ROOT_USR, salt, hashedRootPassword);
                System.out.println("Root user created with username " + ROOT_USR + " and password " + rootPassword);
            }
        }
        return databaseManager;
    }

    public static LinkSessionManager getLinkSessionManager() {
        if (linkSessionManager == null) {
            linkSessionManager = new LinkSessionManager(new WHostDataMessageHandler());
        }
        return linkSessionManager;
    }

    public static void startup() {
        getConnectionListenerDaemon();
        getDatabaseManager();
        getLinkSessionManager();
    }

    public static void shutdown() {
        connectionListenerDaemon.kill();
        databaseManager.shutdown();
    }
}
