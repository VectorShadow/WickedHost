package vsdl.whost.connections;

import vsdl.datavector.crypto.CryptoUtilities;
import vsdl.datavector.crypto.RSA;
import vsdl.datavector.elements.DataMessageBuilder;
import vsdl.datavector.link.LinkSession;
import vsdl.whost.exec.WHostEntityManager;
import vsdl.wl.elements.DataMessageHeaders;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListenerDaemon extends Thread {

    public static final int HOST_PORT = 31592;
    private final ServerSocket SERVER_SOCK;

    private boolean isAlive = true;

    public ConnectionListenerDaemon() {
        try {
            SERVER_SOCK = new ServerSocket(HOST_PORT);
        } catch (IOException e) {
            throw new IllegalStateException("IOException on server socket creation: " + e.getMessage());
        }
    }

    public void kill() {
        isAlive = false;
    }

    public void run() {
        while (isAlive) {
            try {
                Socket s = SERVER_SOCK.accept();
                System.out.println("Accepted new connection on " + s.getLocalPort());
                LinkSession ls = WHostEntityManager.getLinkSessionManager().addSession(s);
                ls.send(
                        DataMessageBuilder
                                .start(DataMessageHeaders.PUBLIC_KEY)
                                .addBlock(CryptoUtilities.toAlphaNumeric(RSA.getSessionPublicKey()))
                                .build()
                );
            } catch (IOException e) { //no need to kill the server here, log the error and continue
                System.out.println("Failed to accept connection");
            }
        }
    }
}
