package vsdl.whost.connections.handlers;

import vsdl.datavector.crypto.CryptoUtilities;
import vsdl.datavector.crypto.Encryption;
import vsdl.wl.wom.BoolWOM;
import vsdl.wl.wom.WickedObjectModel;
import vsdl.wrepo.cql.query.QueryType;

import java.util.List;

import static vsdl.whost.exec.WHostEntityManager.*;

public class LogonHandler {

    private String decryptPassword(String encryptedPassword, int connectionID) {
        return CryptoUtilities.toAlphaNumeric(
                Encryption.encryptDecrypt(
                        getLinkSessionManager()
                                .getSessionByID(connectionID)
                                .getSessionSecret(),
                        CryptoUtilities.fromAlphaNumeric(encryptedPassword)
                )
        );
    }

    private boolean isUserExists(String username) {
        List<WickedObjectModel> results = getDatabaseManager().query(QueryType.LOGIN_EXISTS, username);
        return ((BoolWOM)results.get(0)).value();
    }

    private boolean isCorrectPassword(String username, String decryptedPassword) {
        //todo - query this
        return false;
    }

    public void userLogonRequest(String username, String password, int connectionID) {
        if (!isUserExists(username)) {
            //todo - send account does not exist message to client
            return;
        }
        String decryptedPassword = decryptPassword(password, connectionID);
        System.out.println(
                "Received login data - Username: " +
                        username +
                        " Encrypted Password: " +
                        password +
                        " Decrypted Password: " +
                        decryptedPassword
        );
        if (!isCorrectPassword(username, decryptedPassword)) {
            //todo - send login error message to client
            return;
        }
        //todo - implement queries in WickedDatabaseManager
        //todo - load account data and send to client - should we track the connected account on this end?
    }

    public void userCreationRequest(String username, String password, int connectionID) {
        if (isUserExists(username)) {
            throw new IllegalStateException("Attempted to create user with username " + username +
                    ", but that user already exists.");
        }
        //todo - implement queries in WickedDatabaseManager
        String decryptedPassword = decryptPassword(password, connectionID);
        System.out.println(
                "Received login data - Username: " +
                        username +
                        "\nEncrypted Password: " +
                        password +
                        "\nDecrypted Password: " +
                        decryptedPassword
        );
        //todo - create the new user and return the associated account to the client
    }
}
