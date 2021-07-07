package vsdl.whost.connections;

import vsdl.datavector.link.DataLink;

public class Connection {
    public static long sessionID = 0;

    private final DataLink LINK;
    private final long ID;

    public Connection(DataLink l) {
        LINK = l;
        ID = sessionID++;
        l.start();
    }

    public long getId() {
        return ID;
    }

    public DataLink getDataLink() {
        return LINK;
    }
}
