package Server;

import Servlet.ServletHandler;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int port;
    private ServletHandler handler;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws Exception {

        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new SocketManager(clientSocket, handler)).start();
        }
    }

    public void setHandler(ServletHandler handler) {
        this.handler = handler;
    }
}
