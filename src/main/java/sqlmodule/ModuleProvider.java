package sqlmodule;

import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import sqlmodule.connector.SQLConnector;
import sqlmodule.connector.type.impl.LiteSQLType;
import sqlmodule.connector.type.impl.MySQLType;

import java.io.File;
import java.util.Objects;

@Data
public abstract class ModuleProvider {

    private final Plugin plugin;
    private final String name;

    public ModuleProvider of = this;

    public ModuleProvider(Plugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public SQLConnector setup(String databaseType) {
        final FileConfiguration fileConfiguration = plugin.getConfig();
        final ConfigurationSection databaseConfiguration = fileConfiguration.getConfigurationSection("database");

        final String sqlType;
        if (databaseType != null) {
            sqlType = databaseType;
        } else {
            assert databaseConfiguration != null;
            sqlType = databaseConfiguration.getString("type");
        }

        SQLConnector sqlConnector;

        switch (Objects.requireNonNull(sqlType)) {
            case "mysql": {
                assert databaseConfiguration != null;
                final ConfigurationSection mysqlSection = databaseConfiguration.getConfigurationSection("mysql");

                assert mysqlSection != null;
                sqlConnector = mysqlDatabaseType(mysqlSection).connect();
                return sqlConnector;
            }

            case "sqlite": {
                assert databaseConfiguration != null;
                final ConfigurationSection sqliteSection = databaseConfiguration.getConfigurationSection("sqlite");

                assert sqliteSection != null;
                sqlConnector = sqliteDatabaseType(sqliteSection).connect();
                return sqlConnector;
            }

            default: {
                plugin.getLogger().severe("database type invalid.");
                return null;
            }
        }
    }

    private LiteSQLType sqliteDatabaseType(ConfigurationSection configurationSection) {
        return LiteSQLType.builder()
                .file(new File(plugin.getDataFolder(), Objects.requireNonNull(configurationSection.getString(name))))
                .build();
    }

    private MySQLType mysqlDatabaseType(ConfigurationSection configurationSection) {
        return MySQLType.builder()
                .address(configurationSection.getString("address"))
                .username(configurationSection.getString("username"))
                .password(configurationSection.getString("password"))
                .database(configurationSection.getString("database"))
                .build();
    }
}