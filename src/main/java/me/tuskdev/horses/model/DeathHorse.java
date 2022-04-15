package me.tuskdev.horses.model;

import org.bukkit.entity.Horse;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeathHorse {

    private final int id;
    private final String name;
    private final Horse.Color color;
    private final boolean male;
    private final int speed, life, jump, armor;

    public DeathHorse(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.name = resultSet.getString("name");
        this.color = Horse.Color.valueOf(resultSet.getString("color"));
        this.male = resultSet.getBoolean("male");
        this.speed = resultSet.getInt("speed");
        this.life = resultSet.getInt("life");
        this.jump = resultSet.getInt("jump");
        this.armor = resultSet.getInt("armor");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Horse.Color getColor() {
        return color;
    }

    public boolean isMale() {
        return male;
    }

    public int getSpeed() {
        return speed;
    }

    public int getLife() {
        return life;
    }

    public int getJump() {
        return jump;
    }

    public int getArmor() {
        return armor;
    }
}
