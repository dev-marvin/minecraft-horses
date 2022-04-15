package me.tuskdev.horses.controller;

import me.tuskdev.horses.PooledConnection;
import me.tuskdev.horses.model.DeathHorse;
import org.bukkit.entity.Horse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DeathHorseController {

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `death_horses` (`id` INT AUTO_INCREMENT PRIMARY KEY, `owner` CHAR(36) NOT NULL, `name` CHAR(16) NOT NULL, `color` CHAR(10) NOT NULL, `male` BOOLEAN NOT NULL, `speed` INT(1) NOT NULL, `life` INT(1) NOT NULL, `jump` INT(1) NOT NULL, `armor` INT(1) NOT NULL);";
    private static final String QUERY_INSERT_HORSE = "INSERT INTO `death_horses`(`owner`, `name`, `color`, `male`, `speed`, `life`, `jump`, `armor`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String QUERY_DELETE_HORSE = "DELETE FROM `death_horses` WHERE `id` = ?;";
    private static final String QUERY_SELECT_HORSES = "SELECT * FROM `death_horses` WHERE `owner` = ?;";

    private final PooledConnection pooledConnection;

    public DeathHorseController(PooledConnection pooledConnection) {
        this.pooledConnection = pooledConnection;
        createTable();
    }

    void createTable() {
        pooledConnection.async(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(QUERY_CREATE_TABLE);
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void insert(UUID owner, String name, Horse.Color color, boolean male, int speed, int life, int jump, int armor) {
        pooledConnection.async(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(QUERY_INSERT_HORSE);
                preparedStatement.setString(1, owner.toString());
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, color.name());
                preparedStatement.setBoolean(4, male);
                preparedStatement.setInt(5, speed);
                preparedStatement.setInt(6, life);
                preparedStatement.setInt(7, jump);
                preparedStatement.setInt(8, armor);
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void delete(int id) {
        pooledConnection.async(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(QUERY_DELETE_HORSE);
                preparedStatement.setInt(1, id);
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public Set<DeathHorse> selectAll(UUID owner) {
        CompletableFuture<Set<DeathHorse>> completableFuture = new CompletableFuture<>();

        pooledConnection.async(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(QUERY_SELECT_HORSES);
                preparedStatement.setString(1, owner.toString());

                Set<DeathHorse> deathHorses = new HashSet<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) deathHorses.add(new DeathHorse(resultSet));

                completableFuture.complete(deathHorses);

                resultSet.close(); // preparedStatement auto close this resultSet
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return completableFuture.join();
    }

}
