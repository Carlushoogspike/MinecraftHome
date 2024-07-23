package sqlmodule.connector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sqlmodule.connector.type.SQLType;

import java.sql.Connection;
import java.util.function.Consumer;

/**
 * Classe abstrata que fornece uma conexão com o banco de dados.
 * A classe é responsável por fornecer uma conexão ao banco de dados
 * e executar operações em cima dessa conexão através de um {@link Consumer}.
 */
@Getter
@RequiredArgsConstructor
public abstract class SQLConnector {

    /**
     * Tipo do banco de dados para o qual esta conexão é configurada.
     */
    private final SQLType databaseType;

    /**
     * Abstração para fornecer uma conexão com o banco de dados e permitir
     * a execução de operações utilizando essa conexão.
     *
     * @param consumer Um {@link Consumer} que recebe a conexão e executa operações
     *                 com ela. A implementação concreta deve garantir que a conexão
     *                 seja fornecida e que o {@code consumer} seja executado com sucesso.
     */
    public abstract void consumeConnection(Consumer<Connection> consumer);

}
