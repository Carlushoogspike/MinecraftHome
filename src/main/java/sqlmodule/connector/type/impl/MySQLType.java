package sqlmodule.connector.type.impl;

import lombok.Builder;
import sqlmodule.connector.type.RemoteType;

/**
 * Implementação concreta da classe {@link RemoteType} para bancos de dados MySQL.
 * Esta classe configura o tipo de conexão específico para MySQL, utilizando o driver JDBC e a URL apropriados.
 */
public class MySQLType extends RemoteType {

    /**
     * Construtor que configura o tipo de conexão para MySQL.
     *
     * @param address Endereço do servidor MySQL.
     * @param username Nome de usuário para a conexão com o banco de dados.
     * @param password Senha para a conexão com o banco de dados.
     * @param database Nome do banco de dados a ser conectado.
     */
    @Builder
    public MySQLType(String address, String username, String password, String database) {
        super(
                "com.mysql.cj.jdbc.Driver", // Nome da classe do driver JDBC para MySQL
                "jdbc:mysql://" + address + "/" + database, // URL JDBC para a conexão com o banco de dados
                username, // Nome de usuário
                password // Senha
        );
    }
}
