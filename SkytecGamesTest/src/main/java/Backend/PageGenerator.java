package Backend;

import Database.Duel;
import Database.UserDataSet;

public class PageGenerator {

    public static String getMainPage(String username) {

        StringBuilder page = new StringBuilder();

        page.append(head("Main page"));
        page.append("Welcome, " + username + "!");
        page.append(button("get", "duel", "Duel"));
        page.append(button("post","logout", "Exit"));
        page.append(endOfPage());

        return page.toString();
    }

    public static String getAuthorizationPage(String message) {

        StringBuilder page = new StringBuilder();

        page.append(head("Authorization page"));
        page.append(message);
        page.append(authorizationForm());
        page.append(endOfPage());

        return page.toString();
    }

    public static String getDuelPage(int rating) {

        StringBuilder page = new StringBuilder();

        page.append(head("Duel page"));
        page.append("Your rating: " + rating);
        page.append(button("post", "duel", "Start Duel"));
        page.append(button("post", "toMainPage", "Main page"));
        page.append(endOfPage());

        return page.toString();
    }

    public static String getDuelReadyPage(String message, Duel duel) {

        StringBuilder page = new StringBuilder();

        page.append(head("Duel page"));
        page.append(message);
        page.append(pregameTable(duel.getPlayer1(), duel.getPlayer2()));
        page.append(endOfPage());

        return page.toString();
    }

    public static String getDuelRunningPage(String username, Duel duel) {

        StringBuilder page = new StringBuilder();

        page.append(head("Duel"));
        page.append(gameTable(username, duel));
        page.append(button("post", "attack", "Attack"));
        page.append(duelLog(username, duel));
        page.append(endOfPage());

        return page.toString();
    }

    public static String getDuelPendingPage() {

        StringBuilder page = new StringBuilder();

        page.append(head("Pending"));
        page.append("Your status: ready<br> Waiting for the opponent...\n");
        page.append(button("post", "toMainPage", "Cancel"));
        page.append(endOfPage());

        return page.toString();
    }

    public static String getDuelEndPage(String username, Duel duel) {

        StringBuilder page = new StringBuilder();

        page.append(head("The duel is over.<br>"));
        page.append(getCheckedString(username, duel.getWinner(),
                "Congratulations you won!<br>\n", "Unfortunately, you lost.<br>\n"));
        page.append("Player ");
        page.append(duel.getWinner());
        page.append(" got +1 damage, +1 health and +1 rating.<br>Player ");
        page.append(duel.getLooser());
        page.append(" got +1 damage, +1 health and -1 rating.<br>");
        page.append(button("post", "toMainPage", "Main page"));
        page.append(button("get", "duel", "Duel"));
        page.append(endOfPage());

        return page.toString();
    }

    public static String getPage(String message) {

        StringBuilder page = new StringBuilder();

        page.append(head("No title"));
        page.append(message);
        page.append(endOfPage());

        return page.toString();
    }

    private static String head(String title) {

        return  "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <link rel=\"icon\" href=\"favicon.ico\" type=\"image/x-icon\" />" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>" + title + "</title>\n" +
                "</head>\n" +
                "<body>\n";
    }

    private static String button(String method, String action, String value) {

        StringBuilder buttonElement = new StringBuilder();

        buttonElement.append("<form method=\"");
        buttonElement.append(method);
        buttonElement.append("\" action=\"");
        buttonElement.append(action);
        buttonElement.append("\">\n<input type=\"submit\" value=\"");
        buttonElement.append(value);
        buttonElement.append("\">\n</form>\n");

        return buttonElement.toString();
    }

    private static String authorizationForm() {
        return  "<form method=\"post\" action=\"signin\">\n" +
                "    Name: <input type=\"text\" name=\"login\">\n" +
                "    Password: <input type=\"password\" name=\"password\">\n" +
                "    <input type=\"submit\" value=\"Sign in\">\n" +
                "</form>\n";
    }

    private static String pregameTable(UserDataSet player1, UserDataSet player2) {

        StringBuilder table = new StringBuilder();

        table.append("<table>\n" );
        table.append("<tr><th></th><th>Player 1</th><th>Player 2</th></tr>\n<tr><td>");

        table.append("Name:");
        table.append("</td><td>");
        table.append(player1.getName());
        table.append("</td><td>");
        table.append(player2.getName());
        table.append("</td></tr>\n<tr><td>");

        table.append("Rating:");
        table.append("</td><td>");
        table.append(player1.getRating());
        table.append("</td><td>");
        table.append(player2.getRating());
        table.append("</td></tr>\n<tr><td>");

        table.append("Damage:");
        table.append("</td><td>");
        table.append(player1.getDamage());
        table.append("</td><td>");
        table.append(player2.getDamage());
        table.append("</td></tr>\n<tr><td>");

        table.append("Health:");
        table.append("</td><td>");
        table.append(player1.getMaxHealth());
        table.append("</td><td>");
        table.append(player2.getMaxHealth());
        table.append("</td></tr>\n</table>");

        return table.toString();
    }

    private static String gameTable(String username, Duel duel) {

        StringBuilder table = new StringBuilder();
        UserDataSet player1 = duel.getPlayer1();
        UserDataSet player2 = duel.getPlayer2();

        table.append("<table>\n<tr><th>");
        table.append(getCheckedString(username, duel.getPlayer1().getName(),
                "You", "Opponent"));
        table.append("</th><th>");
        table.append(getCheckedString(username, duel.getPlayer2().getName(),
                "You", "Opponent"));
        table.append("</th></tr>\n<tr><td>");

        table.append(player1.getName());
        table.append("</td><td>");
        table.append(player2.getName());
        table.append("</td></tr>\n<tr><td>Damage ");

        table.append(player1.getDamage());
        table.append("</td><td>Damage ");
        table.append(player2.getDamage());
        table.append("</td></tr>\n<tr><td>Health ");

        table.append(player1.getHealth());
        table.append('/');
        table.append(player1.getMaxHealth());
        table.append("</td><td>Health ");
        table.append(player2.getHealth());
        table.append('/');
        table.append(player2.getMaxHealth());
        table.append("</td></tr>\n<tr><td>");

        table.append("<progress max=\"");
        table.append(player1.getMaxHealth());
        table.append("\" value=\"");
        table.append(player1.getHealth());
        table.append("\">\n</progress>\n</td><td>\n<progress max=\"");
        table.append(player2.getMaxHealth());
        table.append("\" value=\"");
        table.append(player2.getHealth());
        table.append("\">\n</progress>\n</td></tr>\n</table>");

        return table.toString();
    }

    private static String duelLog(String username, Duel duel) {
        StringBuilder duelLog = new StringBuilder();

        for (String log : duel.getLog()) {
            duelLog.append(log.replace(username, "You"));
            duelLog.append("<br>");
        }

        return duelLog.toString();
    }

    private static String endOfPage() {
        return "</body>\n</html>";
    }

    private static String getCheckedString(String a, String b, String c, String d) {
        if (a.equals(b))
            return c;
        else
            return d;
    }
}
