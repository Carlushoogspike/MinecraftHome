package sqlmodule.executor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sqlmodule.connector.SQLConnector;
import sqlmodule.executor.adapter.DataResultAdapter;
import sqlmodule.executor.adapter.DataResultAdapterProvider;
import sqlmodule.executor.result.DataResultSet;
import sqlmodule.executor.statment.DataBaseStatement;
import sqlmodule.utils.SQLLogger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Classe responsável por executar consultas SQL e processar os resultados.
 * Utiliza uma conexão fornecida pelo {@link SQLConnector} para realizar operações no banco de dados.
 */
@Getter
@RequiredArgsConstructor
public class SQLExecutor {

    private final SQLConnector sqlConnector;

    /**
     * Executa uma consulta de atualização no banco de dados.
     *
     * @param query A consulta SQL de atualização a ser executada.
     * @param consumer Um {@link Consumer} que aceita um {@link DataBaseStatement} para configurar a consulta.
     */
    public void updateQuery(String query, Consumer<DataBaseStatement> consumer) {
        sqlConnector.consumeConnection(connection -> {
            try (DataBaseStatement statement = DataBaseStatement.of(connection.prepareStatement(query))) {
                consumer.accept(statement);
                statement.executeUpdate();
            } catch (Exception e) {
                SQLLogger.warning("[EXECUTOR] " + e.getMessage());
            }
        });
    }

    /**
     * Executa uma consulta de atualização no banco de dados sem configuração adicional.
     *
     * @param query A consulta SQL de atualização a ser executada.
     */
    public void updateQuery(String query) {
        updateQuery(query, preparedStatement -> {});
    }

    /**
     * Executa uma consulta SQL que retorna um resultado e o processa.
     *
     * @param query A consulta SQL a ser executada.
     * @param consumer Um {@link Consumer} que aceita um {@link DataBaseStatement} para configurar a consulta.
     * @param function Uma {@link Function} que aplica o resultado da consulta {@link DataResultSet}.
     * @param <T> O tipo do resultado esperado.
     * @return O resultado processado da consulta.
     */
    public <T> T resultQuery(String query, Consumer<DataBaseStatement> consumer, Function<DataResultSet, T> function) {
        AtomicReference<T> value = new AtomicReference<>();
        sqlConnector.consumeConnection(connection -> {
            try (DataBaseStatement statement = DataBaseStatement.of(connection.prepareStatement(query))) {
                consumer.accept(statement);

                try (DataResultSet dataResultSet = statement.executeQuery()) {
                    value.set(function.apply(dataResultSet));
                } catch (Exception e) {
                    SQLLogger.warning("[EXECUTOR] " + e.getMessage());
                }

            } catch (Exception e) {
                SQLLogger.warning("[EXECUTOR] " + e.getMessage());
            }
        });
        return value.get();
    }

    /**
     * Executa uma consulta SQL que retorna um único resultado adaptado por um adaptador.
     *
     * @param query A consulta SQL a ser executada.
     * @param consumer Um {@link Consumer} que aceita um {@link DataBaseStatement} para configurar a consulta.
     * @param resultAdapterClass A classe do adaptador de resultado a ser usado para adaptar o resultado da consulta.
     * @param <T> O tipo do resultado esperado.
     * @return O resultado adaptado da consulta, ou {@code null} se não houver resultados.
     */
    public <T> T resultOneQuery(String query,
                                Consumer<DataBaseStatement> consumer,
                                Class<? extends DataResultAdapter<T>> resultAdapterClass
    ) {
        return resultQuery(query, consumer, dataResultSet -> {
            if (dataResultSet.next()) {
                DataResultAdapterProvider adapterProvider = DataResultAdapterProvider.getInstance();
                DataResultAdapter<T> adapter = adapterProvider.getAdapter(resultAdapterClass);
                return adapter.adaptResult(dataResultSet);
            }

            return null;
        });
    }

    /**
     * Executa uma consulta SQL que retorna múltiplos resultados adaptados por um adaptador.
     *
     * @param query A consulta SQL a ser executada.
     * @param consumer Um {@link Consumer} que aceita um {@link DataBaseStatement} para configurar a consulta.
     * @param resultAdapterClass A classe do adaptador de resultado a ser usado para adaptar os resultados da consulta.
     * @param <T> O tipo dos resultados esperados.
     * @return Um conjunto dos resultados adaptados da consulta.
     */
    public <T> Set<T> resultManyQuery(String query,
                                      Consumer<DataBaseStatement> consumer,
                                      Class<? extends DataResultAdapter<T>> resultAdapterClass
    ) {
        return this.resultQuery(query, consumer, dataResultSet -> {
            DataResultAdapterProvider adapterProvider = DataResultAdapterProvider.getInstance();
            DataResultAdapter<T> adapter = adapterProvider.getAdapter(resultAdapterClass);

            Set<T> elements = new LinkedHashSet<>();
            while (dataResultSet.next()) {
                elements.add(adapter.adaptResult(dataResultSet));
            }

            return elements;
        });
    }
}
