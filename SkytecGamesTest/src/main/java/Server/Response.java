package Server;

import java.io.IOException;
import java.io.OutputStream;

public class Response {

    private OutputStream outputStream;
    public static long timeInMs; // todo private

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.timeInMs = System.currentTimeMillis();
    }

    public void write(String s) {
        System.out.println(System.currentTimeMillis() - timeInMs);
        try {
            String timeString = "page: " + (System.currentTimeMillis() - timeInMs) + " ms";
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: Localhost\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + (s.length() + timeString.length()) + "\r\n" +
                    "Connection: close\r\n\r\n" + s + timeString;

            outputStream.write(response.getBytes());
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
