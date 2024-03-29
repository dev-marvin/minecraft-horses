package me.tuskdev.horses.controller;

import me.tuskdev.horses.PooledConnection;
import me.tuskdev.horses.model.CustomHorse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CustomHorseController {

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `horses` (`id` CHAR(36) PRIMARY KEY, `male` BOOLEAN NOT NULL, `owner` CHAR(36) NOT NULL, `speed` INT(1) DEFAULT 1, `life` INT(1) DEFAULT 1, `jump` INT(1) DEFAULT 1, `resurrection` BOOLEAN DEFAULT false);";
    private static final String QUERY_INSERT_HORSE = "INSERT INTO `horses` (`id`, `male`, `owner`) VALUES (?, ?, ?);";
    private static final String QUERY_REINSERT_HORSE = "INSERT INTO `horses` (`id`, `male`, `owner`, `speed`, `life`, `jump`, `resurrection`) VALUES (?, ?, ?, ?, ?, ?, 1);";
    private static final String QUERY_SELECT_HORSE = "SELECT * FROM `horses` WHERE `id` = ?;";
    private static final String QUERY_DELETE_HORSE = "DELETE FROM `horses` WHERE `id` = ?;";
    private static final String QUERY_UPDATE_OWNER = "UPDATE `horses` SET `owner` = ? WHERE `id` = ?;";
    private static final String QUERY_UPDATE_SPEED = "UPDATE `horses` SET `speed` = ? WHERE `id` = ?;";
    private static final String QUERY_UPDATE_LIFE = "UPDATE `horses` SET `life` = ? WHERE `id` = ?;";
    private static final String QUERY_UPDATE_JUMP = "UPDATE `horses` SET `jump` = ? WHERE `id` = ?;";
    private static final String QUERY_UPDATE_RESURRECTION = "UPDATE `horses` SET `resurrection` = ? WHERE `id` = ?;";

    private final PooledConnection pooledConnection;

    public CustomHorseController(PooledConnection pooledConnection) {
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

    public void insert(UUID uuid, UUID owner, boolean male) {
        pooledConnection.async(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(QUERY_INSERT_HORSE);
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setBoolean(2, male);
                preparedStatement.setString(3, owner.toString());
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void reinsert(UUID uuid, UUID owner, boolean male, int speed, int life, int jump) {
        pooledConnection.async(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(QUERY_REINSERT_HORSE);
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setBoolean(2, male);
                preparedStatement.setString(3, owner.toString());
                preparedStatement.setInt(4, speed);
                preparedStatement.setInt(5, life);
                preparedStatement.setInt(6, jump);
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CustomHorse select(UUID uuid) {
        CompletableFuture<CustomHorse> completableFuture = new CompletableFuture<>();

        pooledConnection.async(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(QUERY_SELECT_HORSE);
                preparedStatement.setString(1, uuid.toString());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) completableFuture.complete(null);
                else completableFuture.complete(new CustomHorse(resultSet));

                resultSet.close(); // preparedStatement auto close this resultSet
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return completableFuture.join();
    }

    public void delete(UUID uuid) {
        pooledConnection.async(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(QUERY_DELETE_HORSE);
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateOwner(UUID uuid, UUID owner) {
        update(QUERY_UPDATE_OWNER, owner.toString(), uuid);
    }

    public void updateSpeed(UUID uuid, int speed) {
        update(QUERY_UPDATE_SPEED, speed, uuid);
    }

    public void updateLife(UUID uuid, int life) {
        update(QUERY_UPDATE_LIFE, life, uuid);
    }

    public void updateJump(UUID uuid, int jump) {
        update(QUERY_UPDATE_JUMP, jump, uuid);
    }

    public void updateResurrection(UUID uuid, boolean resurrection) {
        update(QUERY_UPDATE_RESURRECTION, resurrection, uuid);
    }

    void update(String query, Object value, UUID uuid) {
        pooledConnection.async(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setObject(1, value);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}
