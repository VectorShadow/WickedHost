package vsdl.whost.connections;

import vsdl.datavector.api.DataMessageHandler;
import vsdl.datavector.crypto.CryptoUtilities;
import vsdl.datavector.crypto.Encryption;
import vsdl.datavector.crypto.RSA;
import vsdl.datavector.elements.DataMessage;

import java.util.ArrayList;

import static vsdl.whost.exec.WHostEntityManager.*;
import static vsdl.wl.elements.DataMessageHeaders.*;

public class WHostDataMessageHandler implements DataMessageHandler {
    @Override
    public void handle(DataMessage dataMessage, int id) {
        ArrayList<String> blocks = dataMessage.toBlocks();
        String header = blocks.get(0);
        switch (header) {
            case SESSION_KEY:
                getLinkSessionManager()
                        .getSessionByID(id)
                        .setSessionSecret(
                                RSA.decrypt(
                                        CryptoUtilities
                                                .fromAlphaNumeric(
                                                        blocks.get(1)
                                                )
                                )
                        );
                break;
            case LOGIN_ACCOUNT:
                final String loginUsername = blocks.get(1);
                if (!getDataProvider().queryAccount(loginUsername)) {
                    //todo - send account does not exist message to client
                    break;
                }
                if (!getDataProvider().loginAccount(loginUsername, blocks.get(2))) {
                    //todo - send login error message to client
                    break;
                }
                //todo - load account data and send to client - should we track the connected account on this end?
                System.out.println(
                        "Received login data - Username: " +
                                blocks.get(1) +
                                " Encrypted Password: " +
                                blocks.get(2) +
                                " Decrypted Password: " +
                                CryptoUtilities.toAlphaNumeric(
                                        Encryption.encryptDecrypt(
                                                getLinkSessionManager()
                                                        .getSessionByID(id)
                                                        .getSessionSecret(),
                                        CryptoUtilities
                                                .fromAlphaNumeric(
                                                        blocks.get(2)
                                                )
                                        )
                                )
                );
                break;
            case CREATE_ACCOUNT:
                final String createUsername = blocks.get(1);
                if (getDataProvider().queryAccount(createUsername)) {
                    throw new IllegalArgumentException("Tried to create existing user: " + createUsername);
                }
                final String decryptedPassword =
                        CryptoUtilities.toAlphaNumeric(
                                Encryption.encryptDecrypt(
                                        getLinkSessionManager()
                                                .getSessionByID(id)
                                                .getSessionSecret(),
                                        CryptoUtilities
                                                .fromAlphaNumeric(
                                                        blocks.get(2)
                                                )
                                )
                        );
                getDataProvider().createAccount(createUsername, decryptedPassword);
                //todo - accountProvider.getAccount() and return to client
                System.out.println(
                        "Received login data - Username: " +
                                blocks.get(1) +
                                "\nEncrypted Password: " +
                                blocks.get(2) +
                                "\nDecrypted Password: " +
                                decryptedPassword
                );
                break;
            default:
        }
    }

    @Override
    public void handleDataLinkError(Exception e, int id) {

    }

    @Override
    public void handleDataLinkClosure(int id) {

    }
}
