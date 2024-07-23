package sqlmodule.connector.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sqlmodule.connector.SQLConnector;

/**
 * Classe abstrata que representa o tipo de banco de dados e fornece informações
 * necessárias para a conexão com o banco.
 * Esta classe é usada para configurar e estabelecer uma conexão com um banco de dados específico.
 */
@Getter
@RequiredArgsConstructor
public abstract class SQLType {

    /**
     * Nome da classe do driver JDBC para o banco de dados.
     */
    private final String driverClassName;

    /**
     * URL JDBC para a conexão com o banco de dados.
     */
    private final String jdbcUrl;

    /**
     * Método abstrato para estabelecer uma conexão com o banco de dados
     * utilizando as informações fornecidas.
     *
     * @return Um objeto {@link SQLConnector} que gerencia a conexão com o banco de dados.
     * A implementação concreta deve fornecer uma instância de {@code SQLConnector}
     * configurada para o tipo específico de banco de dados.
     */
    public abstract SQLConnector connect();
}
