package sqlmodule.executor.result;

import lombok.RequiredArgsConstructor;
import sqlmodule.utils.SQLLogger;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe que encapsula um {@link ResultSet} para facilitar o acesso e manipulação dos resultados de uma consulta SQL.
 * Implementa a interface {@link AutoCloseable} para garantir o fechamento apropriado do {@link ResultSet}.
 */
@RequiredArgsConstructor
public class DataResultSet implements AutoCloseable {

    private final ResultSet dataResultSet;

    /**
     * Cria uma instância de {@link DataResultSet} com o {@link ResultSet} fornecido.
     *
     * @param dataResultSet O {@link ResultSet} a ser encapsulado.
     * @return Uma nova instância de {@link DataResultSet}.
     */
    public static DataResultSet of(ResultSet dataResultSet) {
        return new DataResultSet(dataResultSet);
    }

    /**
     * Obtém o valor de uma coluna do {@link ResultSet} e o converte para o tipo especificado.
     *
     * @param column O nome da coluna.
     * @param type O tipo para o qual o valor deve ser convertido.
     * @param <T> O tipo do valor.
     * @return O valor da coluna convertido para o tipo especificado, ou {@code null} se o valor for nulo.
     * @throws UnsupportedOperationException Se o {@link ResultSet} não tiver um resultado.
     * @throws NullPointerException Se ocorrer um erro ao acessar o valor da coluna.
     */
    public <T> T get(String column, Class<T> type) {
        try {
            if (dataResultSet.isBeforeFirst()) {
                throw new UnsupportedOperationException("O ResultSet não contém resultados, use next() para buscar o primeiro resultado!");
            }

            Object value = dataResultSet.getObject(column);

            if (value == null) {
                SQLLogger.warning("O valor do objeto é nulo");
                return null;
            }

            return type.cast(value);
        } catch (SQLException e) {
            SQLLogger.warning(e.getMessage());
            throw new NullPointerException("Coluna \"" + column + "\" não contém elemento");
        }
    }

    /**
     * Obtém o valor de uma coluna do {@link ResultSet}.
     *
     * @param column O nome da coluna.
     * @param <T> O tipo do valor.
     * @return O valor da coluna.
     * @throws UnsupportedOperationException Se o {@link ResultSet} não tiver um resultado.
     * @throws NullPointerException Se ocorrer um erro ao acessar o valor da coluna.
     */
    public <T> T get(String column) {
        try {
            if (dataResultSet.isBeforeFirst()) {
                throw new UnsupportedOperationException("O ResultSet não contém resultados, use next() para buscar o primeiro resultado!");
            }

            return (T) dataResultSet.getObject(column);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Coluna \"" + column + "\" não contém elemento");
        }
    }

    /**
     * Move o cursor para o próximo registro no {@link ResultSet}.
     *
     * @return {@code true} se houver um próximo registro, {@code false} caso contrário.
     */
    public boolean next() {
        try {
            return this.dataResultSet.next();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Fecha o {@link ResultSet}.
     *
     * @throws Exception Se ocorrer um erro ao fechar o {@link ResultSet}.
     */
    @Override
    public void close() throws Exception {
        dataResultSet.close();
    }
}
