package Servlet;

import Database.Duel;
import Database.UserDataSet;
import Database.UserStatus;
import Server.Request;
import Server.Response;

import java.util.List;
import java.util.Map;

public class EmptyServlet  extends Servlet {

    public EmptyServlet(
            Map<String, UserDataSet> userBySession,
            Map<String, UserStatus> userStatusByUsername,
            List<Duel> duels,
            String path) {

        super(userBySession, userStatusByUsername, duels, path);
    }

    public void doGet(Request request, Response response) {

        response.write("");
    }
}
