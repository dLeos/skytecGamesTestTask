package Servlet;

import Database.DBService;
import Database.Duel;
import Database.UserDataSet;
import Database.UserStatus;
import Server.Request;
import Server.Response;

import java.util.List;
import java.util.Map;

public class DuelServlet extends Servlet {

    private DBService dbService;

    public DuelServlet(
            DBService dbService,
            Map<String, UserDataSet> userBySession,
            Map<String, UserStatus> userStatusByUsername,
            List<Duel> duels,
            String path) {

        super(userBySession, userStatusByUsername, duels, path);
        this.dbService = dbService;
    }

    public void doGet(Request request, Response response) {

        if (userBySession.containsKey(getSession(request)) &&
                userStatusByUsername.get(getUsername(request)) == UserStatus.ONLINE) {

            userStatusByUsername.put(getUsername(request), UserStatus.DUEL_MENU);
        }
        super.doGet(request, response);
    }

    public void doPost(Request request, Response response) {

        String opponent;
        String username;

        if (userStatusByUsername.containsValue(UserStatus.PENDING) &&
                !(username = getUsername(request)).equals(opponent = getOpponentName())) {

            addToDuel(opponent, username, response);
        } else {
            addToPending(request, response);
        }

        super.doGet(request, response);
    }

    private void addToDuel(String opponent, String username, Response response) {

        userStatusByUsername.put(opponent, UserStatus.READY);
        userStatusByUsername.put(username, UserStatus.READY);

        duels.add(new Duel(System.currentTimeMillis(),
                dbService.selectUser(opponent),
                dbService.selectUser(username)));
    }

    private void addToPending(Request request, Response response) {

        String username = getUsername(request);

        if (userStatusByUsername.get(username) != UserStatus.READY &&
                userStatusByUsername.get(username) != UserStatus.DUEL)

            userStatusByUsername.put(username, UserStatus.PENDING);
    }

    private String getOpponentName() {

        String opponentName = null;

        for (Map.Entry<String, UserStatus> entry : userStatusByUsername.entrySet()) {
            if (entry.getValue() == UserStatus.PENDING) {
                opponentName = entry.getKey();
                break;
            }
        }

        return opponentName;
    }
}