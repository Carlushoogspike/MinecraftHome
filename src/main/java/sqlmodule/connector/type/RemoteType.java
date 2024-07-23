package sqlmodule.connector.type;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import sqlmodule.connector.SQLConnector;
import sqlmodule.utils.SQLLogger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Classe abstrata que representa um tipo de banco de dados remoto
 * e configura um {@link HikariDataSource} para gerenciar a conexão com o banco.
 * Esta classe é uma extensão de {@link SQLType} e fornece uma implementação
 * concreta para estabelecer conexões com bancos de dados remotos.
 */
@Getter
public abstract class RemoteType extends SQLType {

    /**
     * Instância do {@link HikariDataSource} usada para gerenciar as conexões com o banco de dados.
     */
    private final HikariDataSource dataSource = new HikariDataSource();

    /**
     * Construtor que configura o {@link HikariDataSource} com as informações fornecidas.
     *
     * @param driverClassName Nome da classe do driver JDBC para o banco de dados.
     * @param jdbcUrl URL JDBC para a conexão com o banco de dados.
     * @param username Nome de usuário para autenticação com o banco de dados.
     * @param password Senha para autenticação com o banco de dados.
     */
    public RemoteType(String driverClassName, String jdbcUrl, String username, String password) {
        super(driverClassName, jdbcUrl);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl(jdbcUrl);
    }

    /**
     * Configura o {@link HikariDataSource} usando o {@link Consumer} fornecido.
     *
     * @param consumer Função que aceita um {@link HikariDataSource} e configura suas propriedades.
     * @param <T> Tipo de {@code RemoteType} que está sendo configurado.
     * @return A instância atual de {@code RemoteType} para encadeamento de métodos.
     */
    public <T extends RemoteType> T configureDataSource(Consumer<HikariDataSource> consumer) {
        consumer.accept(dataSource);
        return (T) this;
    }

    @Override
    public SQLConnector connect() {
        return new SQLConnector(this) {

            @Override
            public void consumeConnection(Consumer<Connection> consumer) {
                try (Connection connection = dataSource.getConnection()) {
                    consumer.accept(connection);
                } catch (SQLException e) {
                    SQLLogger.warning(e.getMessage());
                }
            }

        };
    }

}
