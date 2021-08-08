package vsdl.whost.connections.handlers;

import vsdl.datavector.elements.DataMessage;
import vsdl.whost.exec.WHostEntityManager;

public class AbstractHandler {
    protected void sendByID(DataMessage dataMessage, int sessionID) {
        WHostEntityManager.getLinkSessionManager().getSessionByID(sessionID).send(dataMessage);
    }
}
