package Database;

import java.sql.*;

public class DBService {

    private final String INSERT_USER = "INSERT INTO players (name, password, damage, health, rating) VALUES (?, ?, ?, ?, ?);";
    private final String SELECT_USER = "SELECT * FROM players WHERE name = ?;";
    private final String UPDATE_USER_STATS = "UPDATE players SET damage = ?, health = ?, rating = ? WHERE name = ?;";

    private Connection connection;

    public DBService(Connection connection) {
        this.connection = connection;
    }

    public void insertUser(UserDataSet user) {

        try (PreparedStatement statement = connection.prepareStatement(INSERT_USER)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getDamage());
            statement.setInt(4, user.getMaxHealth());
            statement.setInt(5, user.getRating());
            statement.execute();

        } catch (SQLException e) {
            System.out.println("User adding error.");
            e.printStackTrace();
        }
    }

    public UserDataSet selectUser(String username) {

        UserDataSet user = null;

        try (PreparedStatement statement = connection.prepareStatement(SELECT_USER)) {

            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                user = new UserDataSet();
                user.setName(result.getString("name"));
                user.setPassword(result.getString("password"));
                user.setDamage(result.getInt("damage"));
                user.setMaxHealth(result.getInt("health"));
                user.setHealth(result.getInt("health"));
                user.setRating(result.getInt("rating"));
            }

        } catch (SQLException e) {
            System.out.println("User selection error.");
            e.printStackTrace();
        }

        return user;
    }

    public void updateUserStats(UserDataSet userWithNewStats) {

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER_STATS)) {

            statement.setInt(1, userWithNewStats.getDamage());
            statement.setInt(2, userWithNewStats.getMaxHealth());
            statement.setInt(3, userWithNewStats.getRating());
            statement.setString(4, userWithNewStats.getName());
            statement.execute();

        } catch (SQLException e) {
            System.out.println("User updating error.");
            e.printStackTrace();
        }
    }
}
