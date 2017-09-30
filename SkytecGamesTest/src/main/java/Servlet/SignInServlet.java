package Servlet;

import Backend.PageGenerator;
import Database.DBService;
import Database.Duel;
import Database.UserDataSet;
import Database.UserStatus;
import Server.Request;
import Server.Response;

import java.util.List;
import java.util.Map;

public class SignInServlet extends Servlet {

    private DBService dbService;

    public SignInServlet(
            DBService dbService,
            Map<String, UserDataSet> userBySession,
            Map<String, UserStatus> userStatusByUsername,
            List<Duel> duels,
            String path) {

        super(userBySession, userStatusByUsername, duels, path);
        this.dbService = dbService;
    }

    public void doPost(Request request, Response response) {

        if (dbService.selectUser(request.getVariable("login")) != null) {
            authorize(request, response);
        } else {
            register(request, response);
        }
    }

    private void authorize(Request request, Response response) {

        String username = request.getVariable("login");
        String password = request.getVariable("password");
        UserDataSet user = dbService.selectUser(username);

        if (!user.getPassword().equals(password)) {
            response.write(PageGenerator.getAuthorizationPage("Password incorrect!"));
            return;
        }

        userBySession.put(getSession(request), user);
        userStatusByUsername.put(username, getNewUserStatus(user));

        super.doGet(request, response);
    }

    private void register(Request request, Response response) {

        String username = request.getVariable("login");
        String password = request.getVariable("password");
        UserDataSet user = new UserDataSet(username, password);

        dbService.insertUser(user);

        authorize(request, response);

        //response.write(PageGenerator.getAuthorizationPage(
        //        "Register is temporarily unavailable"));
    }

    private UserStatus getNewUserStatus(UserDataSet user) {

        if (!userBySession.containsValue(user))
            return UserStatus.OFFLINE;

        if (!userStatusByUsername.containsKey(user.getName()))
            return UserStatus.ONLINE;

        return  userStatusByUsername.get(user.getName());
    }
}
