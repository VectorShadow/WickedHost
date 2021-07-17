package vsdl.whost.connections;

import vsdl.datavector.api.DataMessageHandler;
import vsdl.datavector.crypto.CryptoUtilities;
import vsdl.datavector.crypto.RSA;
import vsdl.datavector.elements.DataMessage;
import vsdl.datavector.elements.DataMessageBuilder;
import vsdl.datavector.link.DataLink;

import java.math.BigInteger;
import java.util.ArrayList;

import static vsdl.wl.elements.DataMessageHeaders.PUBLIC_KEY;
import static vsdl.wl.elements.DataMessageHeaders.SESSION_KEY;

public class WHostDataMessageHandler implements DataMessageHandler {
    @Override
    public void handle(DataMessage dataMessage, int id) {
        System.out.println("Received message: " + dataMessage.toString());
        ArrayList<String> blocks = dataMessage.toBlocks();
        String header = blocks.get(0);
        switch (header) {
            case SESSION_KEY:
                //todo - save this result as the session key in the connection manager for this link
                System.out.println(CryptoUtilities.toAlphaNumeric(RSA.decrypt(CryptoUtilities.fromAlphaNumeric(blocks.get(1)))));
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
