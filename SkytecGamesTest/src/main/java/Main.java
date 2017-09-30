import Database.*;
import Server.Server;
import Servlet.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Throwable {

        Server server = new Server(8080);
        DBConnector dbConnector = new DBConnector();
        DBService dbService = new DBService(dbConnector.getConnection());

        Map<String, UserDataSet> userBySession = new HashMap<>();
        Map<String, UserStatus> userStatusByUsername = new HashMap<>();
        List<Duel> duels = new ArrayList<>();

        ServletHandler handler = new ServletHandler();
        handler.addServlet(new SignInServlet(dbService, userBySession, userStatusByUsername, duels, "/signin"));
        handler.addServlet(new LogOutServlet(userBySession, userStatusByUsername, duels, "/logout"));
        handler.addServlet(new DuelServlet(dbService, userBySession, userStatusByUsername, duels, "/duel"));
        handler.addServlet(new ToMainPageServlet(userBySession, userStatusByUsername, duels,"/toMainPage"));
        handler.addServlet(new AttackHandlerServlet(dbService, userBySession, userStatusByUsername, duels,"/attack"));
        handler.addServlet(new EmptyServlet(userBySession, userStatusByUsername, duels,"/favicon.ico"));
        handler.addDefaultServlet(new Servlet(userBySession, userStatusByUsername, duels,"/"));

        server.setHandler(handler);
        server.start();
    }
}

