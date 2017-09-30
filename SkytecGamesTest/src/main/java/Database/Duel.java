package Database;

import java.util.ArrayList;
import java.util.List;

public class Duel {

    public static final int WAITING_TIME = 10;
    private UserDataSet user1;
    private UserDataSet user2;
    private long startTime;
    private List<String> log;
    private String winner;
    private String looser;

    public Duel(long startTime, UserDataSet user1, UserDataSet user2) {

        this.startTime = startTime;
        this.user1 = user1;
        this.user2 = user2;
        this.log = new ArrayList<>();
        this.winner = null;
        this.looser = null;
    }

    public void attack(String attacking) {
        if (user1.getName().equals(attacking) &&
                user1.getHealth() > 0) {
            attack(user1, user2);
        }
        else if (user2.getName().equals(attacking) &&
                user2.getHealth() > 0) {
            attack(user2, user1);
        }
    }

    public boolean finished() {
        if (user1.getHealth() <= 0 || user2.getHealth() <= 0)
            return true;

        return false;
    }

    public String getWinner() {
        if (winner == null)
            winner = user1.getHealth() > 0 ? user1.getName() : user2.getName();

        return winner;
    }

    public String getLooser() {
        if (looser == null)
            looser = user1.getHealth() <= 0 ? user1.getName() : user2.getName();

        return looser;
    }

    public long getStartTime() {
        return startTime;
    }

    public UserDataSet getPlayer1() {
        return user1;
    }

    public UserDataSet getPlayer2() {
        return user2;
    }

    public List<String> getLog() {
        return log;
    }

    public void remove(String username) {
        if (user1 != null && user1.getName().equals(username)) user1 = null;
        if (user2 != null && user2.getName().equals(username)) user2 = null;
    }

    private void attack(UserDataSet attaking, UserDataSet attacked) {
        attacked.setHealth(attacked.getHealth() - attaking.getDamage());
        log.add(attaking.getName() + " hit " + attacked.getName() +
                " for " + attaking.getDamage()  + " damage");

        if (attacked.getHealth() <= 0) {
            log.add(attaking.getName() + " killed " + attacked.getName());
        }
    }
}
