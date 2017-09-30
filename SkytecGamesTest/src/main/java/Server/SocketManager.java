package Server;

import Servlet.ServletHandler;

import java.io.IOException;
import java.net.Socket;

public class SocketManager implements Runnable {

    private Socket socket;
    private Request request;
    private Response response;
    private ServletHandler handler;

    public SocketManager(Socket s, ServletHandler handler) throws Exception {
        this.socket = s;
        this.handler = handler;
        this.request = new Request(s.getInputStream());
        this.response = new Response(s.getOutputStream());
    }

    public void run() {
        handler.handle(request, response);
        close();
    }

    private void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}