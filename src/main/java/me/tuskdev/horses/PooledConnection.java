package me.tuskdev.horses;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;

public class PooledConnection {

    private final HikariDataSource hikariDataSource;

    PooledConnection(ConfigurationSection configurationSection) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(configurationSection.getString("url"));
        hikariConfig.setUsername(configurationSection.getString("username"));
        hikariConfig.setPassword(configurationSection.getString("password"));

        configurationSection.getConfigurationSection("dataSourceProperties").getValues(true).forEach(hikariConfig::addDataSourceProperty);

        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    void close() {
        hikariDataSource.close();
    }
}

