package sqlmodule.connector.type;

import lombok.Getter;
import sqlmodule.connector.SQLConnector;
import sqlmodule.utils.SQLLogger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Classe abstrata que representa um tipo de banco de dados baseado em arquivo.
 * Esta classe configura e gerencia uma conexão com um banco de dados local baseado em um arquivo.
 */
@Getter
public abstract class FileType extends SQLType {

    /**
     * O arquivo que representa o banco de dados.
     */
    private final File file;

    /**
     * Construtor que configura a classe do driver JDBC, a URL JDBC e o arquivo do banco de dados.
     *
     * @param driverClassName Nome da classe do driver JDBC para o banco de dados.
     * @param jdbcUrl URL JDBC para a conexão com o banco de dados.
     * @param file Arquivo que representa o banco de dados.
     */
    public FileType(String driverClassName, String jdbcUrl, File file) {
        super(driverClassName, jdbcUrl);
        this.file = file;
    }

    @Override
    public SQLConnector connect() {
        try {
            // Carrega a classe do driver JDBC
            Class.forName(this.getDriverClassName());
            // Estabelece uma conexão com o banco de dados usando a URL JDBC
            Connection connection = DriverManager.getConnection(this.getJdbcUrl());
            return new SQLConnector(this) {

                @Override
                public void consumeConnection(Consumer<Connection> consumer) {
                    // Aceita e utiliza a conexão fornecida
                    consumer.accept(connection);
                }

            };
        } catch (SQLException | ClassNotFoundException t) {
            // Registra um aviso se houver uma exceção ao carregar o driver ou ao estabelecer a conexão
            SQLLogger.warning(t.getMessage());
            throw new NullPointerException("A conexão não pode ser nula");
        }
    }

}
