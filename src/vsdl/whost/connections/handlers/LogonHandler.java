package vsdl.whost.connections.handlers;

import vsdl.datavector.crypto.CryptoUtilities;
import vsdl.datavector.crypto.Encryption;
import vsdl.datavector.elements.DataMessageBuilder;
import vsdl.wl.wom.UserLogonWOM;
import vsdl.wl.wom.WickedObjectModel;
import vsdl.wrepo.cql.query.QueryType;

import java.util.List;

import static vsdl.whost.crypto.SecurityConstants.generateSalt;
import static vsdl.whost.exec.WHostEntityManager.*;
import static vsdl.wl.elements.DataMessageErrors.*;
import static vsdl.wl.elements.DataMessageHeaders.*;

public class LogonHandler extends AbstractHandler {

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

    private UserLogonWOM getUserLogon(String username) {
        List<WickedObjectModel> results = getDatabaseManager().query(QueryType.LOGON_USER_EXISTS, username);
        return ((UserLogonWOM)results.get(0));
    }

    public void userLogonRequest(String username, String password, int connectionID) {
        UserLogonWOM userLogon = getUserLogon(username);
        if (userLogon == null) {
            sendByID(
                    DataMessageBuilder.start(LOGON_ERR).addBlock(LOGON_USER_DID_NOT_EXIST).addBlock(username).build(),
                    connectionID
            );
            return;
        }
        String decryptedPassword = decryptPassword(password, connectionID);
        System.out.println(
                "Received logon user data - Username: " +
                        username +
                        " Encrypted Password: " +
                        password +
                        " Decrypted Password: " +
                        decryptedPassword
        );
        if (!CryptoUtilities.hash(CryptoUtilities.salt(decryptedPassword, userLogon.getSalt()))
                .equals(userLogon.getHashedPassword())) {
            sendByID(
                    DataMessageBuilder.start(LOGON_ERR).addBlock(LOGON_INCORRECT_PASSWORD).build(),
                    connectionID
            );
            return;
        }
        //todo - get characters associated with username and send to client (new query as own method?)
        //todo - we should also save the logon in the connection so we can know what validated account is there
    }

    public void userCreationRequest(String username, String password, int connectionID) {
        if (getUserLogon(username) != null) {
            throw new IllegalStateException("Attempted to create user with username " + username +
                    ", but that user already exists.");
        }
        String decryptedPassword = decryptPassword(password, connectionID);
        System.out.println(
                "Received create user data - Username: " +
                        username +
                        "\nEncrypted Password: " +
                        password +
                        "\nDecrypted Password: " +
                        decryptedPassword
        );
        String salt = generateSalt();
        String hashedPassword = CryptoUtilities.hash(CryptoUtilities.salt(decryptedPassword, salt));
        getDatabaseManager().query(QueryType.LOGON_CREATE_USER, username, salt, hashedPassword);
        //todo - get (empty) characters associated with username and send to client
    }
}
