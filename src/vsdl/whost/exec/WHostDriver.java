package vsdl.whost.exec;

public class WHostDriver {

    public static void main(String[] args) {
        WHostEntityManager.getConnectionListenerDaemon().start();
    }
}
