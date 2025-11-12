package org.example.Console;

import org.example.General;

import java.io.IOException;

public class Server {
    public static boolean run() throws IOException {
        if (General.strings.size() - General.indexString < 2) {
            System.out.println("Error not server command");
            return false;
        }

    q:  switch (General.strings.get(++General.indexString)) {
            case "start" -> {
                General.server = new org.example.Networks.Server.Server();
                General.server.start();
            }
            case "stop" -> {
                if (General.server == null) {
                    System.out.println("Error the server is not running");
                    break q;
                }

                General.server.serverSocket.close();
                General.server.interrupt();
                General.server = null;
            }
            case "info" -> {
                if (General.server == null) {
                    System.out.println("Error the server is not running");
                    break q;
                }

                synchronized (General.server.nodes) {
                    for (org.example.Networks.Server.Server.Node element : General.server.nodes)
                        System.out.println();
                }
            }
            default -> {
                System.out.println("Error not server command: " + General.strings.get(General.indexString));
                return false;
            }
        }

        ++General.indexString;
        return true;
    }
}
