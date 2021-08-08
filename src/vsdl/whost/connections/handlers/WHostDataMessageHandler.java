package vsdl.whost.connections.handlers;

import vsdl.datavector.api.DataMessageHandler;
import vsdl.datavector.crypto.CryptoUtilities;
import vsdl.datavector.crypto.RSA;
import vsdl.datavector.elements.DataMessage;

import java.util.ArrayList;

import static vsdl.whost.exec.WHostEntityManager.*;
import static vsdl.wl.elements.DataMessageHeaders.*;

public class WHostDataMessageHandler implements DataMessageHandler {

    private final LogonHandler logonHandler = new LogonHandler();

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
            case LOGON_USER:
                logonHandler.userLogonRequest(blocks.get(1), blocks.get(2), id);
                break;
            case CREATE_USER:
                logonHandler.userCreationRequest(blocks.get(1), blocks.get(2), id);
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
