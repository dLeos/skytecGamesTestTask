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

public class Servlet {

    protected String path;
    protected Map<String, UserDataSet> userBySession;
    protected Map<String, UserStatus> userStatusByUsername;
    protected List<Duel> duels;

    public Servlet(
            Map<String, UserDataSet> userBySession,
            Map<String, UserStatus> userStatusByUsername,
            List<Duel> duels,
            String path) {

        this.userBySession = userBySession;
        this.userStatusByUsername = userStatusByUsername;
        this.duels = duels;
        this.path = path;
    }

    public void doGet(Request request, Response response) {

        UserStatus status = getUserStatusBySession(getSession(request));

        switch (status) {
            case ONLINE:
                response.write(PageGenerator.getMainPage(getUsername(request)));
                break;
            case DUEL_MENU:
                response.write(PageGenerator.getDuelPage(getRating(request)));
                break;
            case PENDING:
                response.write(PageGenerator.getDuelPendingPage());
                break;
            case READY:
                Duel duel = getDuel(request);
                long timeFromStart = (System.currentTimeMillis() - duel.getStartTime()) / 1000;

                if (timeFromStart > Duel.WAITING_TIME) {
                    userStatusByUsername.put(duel.getPlayer1().getName(), UserStatus.DUEL);
                    userStatusByUsername.put(duel.getPlayer2().getName(), UserStatus.DUEL);
                    doGet(request, response);
                    return;
                }
                response.write(PageGenerator.getDuelReadyPage(
                        "Duel will starts in  " + (Duel.WAITING_TIME - timeFromStart) + " seconds.",
                        duel));
                break;
            case DUEL:
                response.write(PageGenerator.getDuelRunningPage(
                        getUsername(request),
                        getDuel(request)));
                break;
            case DUEL_END:
                Duel finishedDuel = getDuel(request);
                String page = PageGenerator.getDuelEndPage(getUsername(request), finishedDuel);

                userStatusByUsername.put(getUsername(request), UserStatus.ONLINE);

                finishedDuel.remove(getUsername(request));
                if (finishedDuel.getPlayer1() == null &&
                        finishedDuel.getPlayer2() == null)
                    duels.remove(finishedDuel);

                response.write(page);
                break;
            default:
                response.write(PageGenerator.getAuthorizationPage("Authorization form"));
                break;
        }
    }

    public void doPost(Request request, Response response) {

        doGet(request, response);
    }

    public String getPath() {
        return path;
    }

    protected UserStatus getUserStatusBySession(String session) {
        UserStatus status = UserStatus.OFFLINE;

        if (userBySession.containsKey(session)) {
            status = userStatusByUsername.get(userBySession.get(session).getName());
        }

        return status;
    }

    protected String getSession(Request request) {
        return request.getParameter("Cookie");
    }

    protected String getUsername(Request request) {
        String session = getSession(request);

        if (userBySession.containsKey(session))
            return userBySession.get(getSession(request)).getName();

        return null;
    }

    protected int getRating(Request request) {
        return userBySession.get(getSession(request)).getRating();
    }

    protected Duel getDuel(Request request) {

        String username = getUsername(request);

        for (Duel duel : duels) {
            if ((duel.getPlayer1() != null && duel.getPlayer1().getName().equals(username)) ||
                    (duel.getPlayer2() != null && duel.getPlayer2().getName().equals(username)))

                return duel;
        }

        return null;
    }
}
