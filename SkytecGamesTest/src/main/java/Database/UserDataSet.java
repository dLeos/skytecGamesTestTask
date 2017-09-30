package Database;

public class UserDataSet {

    private String name;
    private String password;
    private int damage;
    private int maxHealth;
    private int health;
    private int rating;

    public UserDataSet () {}

    public UserDataSet(String name, String password) {
        this.name = name;
        this.password = password;
        this.damage = 10;
        this.maxHealth = 100;
        this.health = maxHealth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        if (damage > 0) this.damage = damage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int health) {
        if (health > 0) this.maxHealth = health;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDataSet that = (UserDataSet) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
