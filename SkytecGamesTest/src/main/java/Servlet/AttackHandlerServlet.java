package Servlet;

import Database.DBService;
import Database.Duel;
import Database.UserDataSet;
import Database.UserStatus;
import Server.Request;
import Server.Response;

import java.util.List;
import java.util.Map;

public class AttackHandlerServlet extends Servlet {

    private DBService dbService;

    public AttackHandlerServlet(
            DBService dbService,
            Map<String, UserDataSet> userBySession,
            Map<String, UserStatus> userStatusByUsername,
            List<Duel> duels,
            String path) {

        super(userBySession, userStatusByUsername, duels, path);
        this.dbService = dbService;
    }

    public void doPost(Request request, Response response) {

        Duel duel = getDuel(request);
        if (duel.getPlayer1() == null || duel.getPlayer2() == null) {
            super.doGet(request, response);
        }
        duel.attack(getUsername(request));

        if (duel.finished()) {

            userStatusByUsername.put(duel.getPlayer1().getName(), UserStatus.DUEL_END);
            userStatusByUsername.put(duel.getPlayer2().getName(), UserStatus.DUEL_END);

            updateStats(duel);
        }
        super.doGet(request, response);
    }

    private void updateStats(Duel duel) {

        String winnerName = duel.getWinner();
        UserDataSet winner = dbService.selectUser(winnerName);
        winner.setDamage(winner.getDamage() + 1);
        winner.setMaxHealth(winner.getMaxHealth() + 1);
        winner.setRating(winner.getRating() + 1);
        dbService.updateUserStats(winner);

        String looserName = duel.getLooser();
        UserDataSet looser = dbService.selectUser(looserName);
        looser.setDamage(looser.getDamage() + 1);
        looser.setMaxHealth(looser.getMaxHealth() + 1);
        looser.setRating(looser.getRating() - 1);
        dbService.updateUserStats(looser);
    }
}
