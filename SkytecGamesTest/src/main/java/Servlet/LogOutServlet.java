package Servlet;

import Backend.PageGenerator;
import Database.Duel;
import Database.UserDataSet;
import Database.UserStatus;
import Server.Request;
import Server.Response;

import java.util.List;
import java.util.Map;

public class LogOutServlet extends Servlet {

    public LogOutServlet(
            Map<String, UserDataSet> userBySession,
            Map<String, UserStatus> userStatusByUsername,
            List<Duel> duels,
            String path) {

        super(userBySession, userStatusByUsername, duels, path);
    }

    public void doPost(Request request, Response response) {

        String username = getUsername(request);
        String session = getSession(request);

        if (userBySession.containsKey(session)) {
            userBySession.remove(session);
        }
        if (!userBySession.containsValue(username)) {
            userStatusByUsername.remove(username);
        }

        response.write(PageGenerator.getAuthorizationPage(
                "You are logged out"));
    }
}
