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
                //todo - accountprovider class to handle this and create
                break;
            case CREATE_ACCOUNT:
                System.out.println("Received account creation request.");
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
