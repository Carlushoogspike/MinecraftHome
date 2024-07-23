package sqlmodule.executor.statment;

import lombok.RequiredArgsConstructor;
import sqlmodule.executor.result.DataResultSet;
import sqlmodule.utils.SQLLogger;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Classe que encapsula um {@link PreparedStatement} para executar consultas SQL.
 * Implementa a interface {@link AutoCloseable} para garantir o fechamento apropriado do {@link PreparedStatement}.
 */
@RequiredArgsConstructor
public final class DataBaseStatement implements AutoCloseable {

    private final PreparedStatement preparedStatement;

    /**
     * Cria uma instância de {@link DataBaseStatement} com o {@link PreparedStatement} fornecido.
     *
     * @param preparedStatement O {@link PreparedStatement} a ser encapsulado.
     * @return Uma nova instância de {@link DataBaseStatement}.
     */
    public static DataBaseStatement of(PreparedStatement preparedStatement) {
        return new DataBaseStatement(preparedStatement);
    }

    /**
     * Define o valor de um parâmetro na consulta SQL.
     *
     * @param parameterIndex O índice do parâmetro a ser definido.
     * @param value O valor a ser definido para o parâmetro.
     */
    public void set(int parameterIndex, Object value) {
        try {
            preparedStatement.setObject(parameterIndex, value);
        } catch (SQLException t) {
            SQLLogger.warning("[STATEMENT] " + t.getMessage());
        }
    }

    /**
     * Executa uma consulta SQL de atualização (INSERT, UPDATE, DELETE).
     */
    public void executeUpdate() {
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException t) {
            SQLLogger.warning("[STATEMENT] " + t.getMessage());
        }
    }

    /**
     * Executa uma consulta SQL que retorna um {@link DataResultSet}.
     *
     * @return Um {@link DataResultSet} contendo os resultados da consulta.
     * @throws NullPointerException Se o {@link DataResultSet} for nulo.
     */
    public DataResultSet executeQuery() {
        try {
            return new DataResultSet(preparedStatement.executeQuery());
        } catch (SQLException t) {
            SQLLogger.warning("[STATEMENT] " + t.getMessage());
            throw new NullPointerException("ResultSet não pode ser nulo");
        }
    }

    /**
     * Fecha o {@link PreparedStatement}.
     *
     * @throws Exception Se ocorrer um erro ao fechar o {@link PreparedStatement}.
     */
    @Override
    public void close() throws Exception {
        preparedStatement.close();
    }
}
