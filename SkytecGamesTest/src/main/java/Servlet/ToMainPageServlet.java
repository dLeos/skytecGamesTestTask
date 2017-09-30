package Servlet;

import Database.Duel;
import Database.UserDataSet;
import Database.UserStatus;
import Server.Request;
import Server.Response;

import java.util.List;
import java.util.Map;

public class ToMainPageServlet extends Servlet {

    public ToMainPageServlet(
            Map<String, UserDataSet> userBySession,
            Map<String, UserStatus> userStatusByUsername,
            List<Duel> duels,
            String path) {

        super(userBySession, userStatusByUsername, duels, path);
    }

    public void doPost(Request request, Response response) {

        userStatusByUsername.put(
                getUsername(request),
                UserStatus.ONLINE);

        super.doGet(request, response);
    }
}
