package me.tuskdev.horses.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CustomHorse {

    private final UUID uniqueId;
    private UUID owner;
    private final boolean male;
    private int speed = 1, life = 1, jump = 1;
    private boolean resurrection = false;

    public CustomHorse(UUID uniqueId, UUID owner) {
        this.uniqueId = uniqueId;
        this.owner = owner;
        this.male = (Math.random() * 100 > 40);
    }

    public CustomHorse(ResultSet resultSet) throws SQLException {
        this.uniqueId = UUID.fromString(resultSet.getString("id"));
        this.owner = UUID.fromString(resultSet.getString("owner"));
        this.male = resultSet.getBoolean("male");
        this.speed = resultSet.getInt("speed");
        this.life = resultSet.getInt("life");
        this.jump = resultSet.getInt("jump");
        this.resurrection = resultSet.getBoolean("resurrection");
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public boolean isMale() {
        return male;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public boolean hasResurrection() {
        return resurrection;
    }

    public void setResurrection(boolean resurrection) {
        this.resurrection = resurrection;
    }
}
