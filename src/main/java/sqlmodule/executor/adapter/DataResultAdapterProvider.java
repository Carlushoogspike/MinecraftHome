package sqlmodule.executor.adapter;

import lombok.Getter;
import sqlmodule.utils.SQLLogger;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fornecedor de adaptadores de resultados de dados que gerencia a criação e fornecimento de instâncias de {@link DataResultAdapter}.
 * Utiliza um padrão de projeto singleton para garantir uma única instância dessa classe.
 */
public final class DataResultAdapterProvider {

    @Getter
    private static final DataResultAdapterProvider instance = new DataResultAdapterProvider();

    private final Map<Class<? extends DataResultAdapter<?>>, DataResultAdapter<?>> adapterMap = new LinkedHashMap<>();

    /**
     * Obtém uma instância do adaptador para a classe fornecida. Se o adaptador ainda não foi criado, ele será instanciado.
     *
     * @param clazz A classe do adaptador desejado.
     * @param <T> O tipo do adaptador.
     * @return Uma instância do adaptador para a classe fornecida.
     * @throws NullPointerException Se ocorrer um erro ao criar o adaptador.
     * @throws RuntimeException Se ocorrer um erro ao invocar o construtor do adaptador.
     */
    public <T extends DataResultAdapter<?>> T getAdapter(Class<T> clazz) {
        return clazz.cast(adapterMap.computeIfAbsent(clazz, k -> {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                SQLLogger.warning("[DataResult] " + e.getMessage());
                throw new NullPointerException("O adaptador não pode ser nulo");
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }));
    }

}
