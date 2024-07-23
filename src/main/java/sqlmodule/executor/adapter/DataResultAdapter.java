package sqlmodule.executor.adapter;

import sqlmodule.executor.result.DataResultSet;

/**
 * Interface que define o contrato para adaptadores de resultados de dados.
 * Um adaptador de resultado de dados é responsável por converter um {@link DataResultSet}
 * em uma instância de um tipo específico.
 *
 * @param <T> O tipo do objeto que o adaptador irá criar a partir do {@link DataResultSet}.
 */
public interface DataResultAdapter<T> {

    /**
     * Adapta um {@link DataResultSet} para uma instância do tipo {@code T}.
     *
     * @param dataResultSet O {@link DataResultSet} que contém os dados a serem convertidos.
     * @return Uma instância do tipo {@code T} criada a partir dos dados do {@link DataResultSet}.
     */
    T adaptResult(DataResultSet dataResultSet);

}
