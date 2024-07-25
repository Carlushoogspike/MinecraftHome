package com.carlos.plugins.tst.sql.adapter;

import com.carlos.plugins.tst.model.Home;
import com.carlos.plugins.tst.model.UserHome;
import com.google.common.reflect.TypeToken;
import org.bukkit.Location;
import sqlmodule.executor.adapter.DataResultAdapter;
import sqlmodule.executor.result.DataResultSet;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

import static com.carlos.plugins.tst.sql.repository.SQLPath.GSON;

/**
 * A classe SQLAdapter é responsável por adaptar o conjunto de resultados de uma consulta SQL
 * em um objeto UserHome. Ela implementa a interface DataResultAdapter e fornece a
 * implementação específica para o método adaptResult.
 */
public class SQLAdapter implements DataResultAdapter<UserHome> {

    // TypeToken para representar o tipo Map<String, Home>
    private final Type homesType = new TypeToken<Map<String, Home>>() {}.getType();

    /**
     * Adapta o DataResultSet fornecido em um objeto UserHome.
     *
     * @param dataResultSet o DataResultSet a ser adaptado
     * @return o objeto UserHome adaptado
     */
    @Override
    public UserHome adaptResult(DataResultSet dataResultSet) {
        // Extrai o UUID do conjunto de resultados e o converte para um objeto UUID
        final UUID uuid = UUID.fromString(dataResultSet.get("uuid").toString());

        // Extrai o nome do conjunto de resultados
        final String name = dataResultSet.get("name");

        // Extrai os dados de homes do conjunto de resultados e os converte de JSON para um Map<String, Home>
        final Map<String, Home> homes = GSON.fromJson((String) dataResultSet.get("homes"), homesType);

        // Constrói e retorna o objeto UserHome
        return UserHome.builder()
                .uuid(uuid)
                .name(name)
                .homeList(homes)
                .build();
    }
}
