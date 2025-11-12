package org.example.Networks.Server;

import org.example.DB.Base;

import java.io.IOException;
import java.util.TreeMap;

public class IOBase {
    public final TreeMap<Integer, Base> map = new TreeMap<>();

    public boolean run(Server.Node client) throws IOException {
        System.out.println(client.dataInputStream.readUTF());
        return true;
    }
}
