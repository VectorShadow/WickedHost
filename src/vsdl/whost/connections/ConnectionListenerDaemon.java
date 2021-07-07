package vsdl.whost.connections;

import vsdl.whost.exec.WHostEntityManager;

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
                WHostEntityManager.getConnectionManager().connectOnSocket(s);
            } catch (IOException e) { //no need to kill the server here, log the error and continue
                System.out.println("Failed to accept connection");
            }
        }
    }
}
