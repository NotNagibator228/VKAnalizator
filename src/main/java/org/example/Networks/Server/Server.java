package org.example.Networks.Server;

import org.example.Colors;
import org.example.General;

import java.io.IOException;
import java.net.ServerSocket;

import java.io.*;
import java.net.*;
import java.util.*;


public class Server extends Thread {
    public class Node extends Thread {
        public int index;
        public final Socket socket;
        public final DataInputStream dataInputStream;
        public final DataOutputStream dataOutputStream;
        public final IOBase ioBase = new IOBase();

        public Node(Socket socket, int index) throws IOException {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            this.socket = socket;
        }

        public void close() throws IOException {
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (interrupted()) {
                        try {
                            this.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        synchronized (nodes) { nodes.remove(index); }
                    } if (!ioBase.run(this)) break;
                } catch (IOException e) {
                    System.out.println(Colors.ANSI_RED + "IO Network Error in: " + this + Colors.ANSI_RESET);
                    try {
                        this.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    synchronized (nodes) { nodes.remove(index); }
                }
            }
        }

        @Override
        public String toString() { return socket.getLocalAddress() + ":" + socket.getPort(); }
    }

    public final ServerSocket serverSocket;
    public final ArrayList<Node> nodes = new ArrayList<>();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(General.port);
    }

    public void addClient() throws IOException {
        Socket socket = serverSocket.accept();
        synchronized (nodes) {
            Node node = new Node(socket, nodes.size());
            node.start();
            nodes.add(node);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (interrupted()) return;
                addClient();
            } catch (IOException e) {
                return;
            }
        }
    }

    void clear() throws IOException {
        synchronized (nodes) {
            for (Node element : nodes)
                element.close();
            nodes.clear();
        }
    }
}
