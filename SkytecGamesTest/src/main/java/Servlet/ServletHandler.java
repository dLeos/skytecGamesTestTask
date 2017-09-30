package Servlet;

import Server.Request;
import Server.Response;

import java.util.ArrayList;
import java.util.List;

public class ServletHandler {

    List<Servlet> servlets;
    Servlet defaultServlet;

    public ServletHandler() {
        servlets = new ArrayList<>();
    }

    public void addServlet(Servlet servlet) {
        servlets.add(servlet);
    }

    public void addDefaultServlet(Servlet servlet) {
        defaultServlet = servlet;
    }

    public void handle(Request request, Response response) {

        Servlet servlet = findHandleServlet(request.getPath());

        if (request.getMethod().equals("GET")) {
            servlet.doGet(request, response);
        }
        else if (request.getMethod().equals("POST")) {
            servlet.doPost(request, response);
        }

    }

    private Servlet findHandleServlet(String path) {

        for (Servlet servlet : servlets) {
            if (servlet.getPath().equals(path)) return servlet;
        }
        return defaultServlet;
    }
}
