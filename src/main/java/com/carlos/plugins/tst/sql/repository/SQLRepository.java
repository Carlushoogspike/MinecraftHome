package com.carlos.plugins.tst.sql.repository;

import com.carlos.plugins.tst.model.UserHome;
import com.carlos.plugins.tst.sql.adapter.SQLAdapter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import sqlmodule.executor.SQLExecutor;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.carlos.plugins.tst.sql.repository.SQLPath.*;

/**
 * A classe SQLRepository é responsável por interagir com o banco de dados,
 * fornecendo métodos para criar tabelas, inserir, atualizar e selecionar registros.
 */
@RequiredArgsConstructor
public class SQLRepository {

    // Executor SQL para executar consultas e atualizações no banco de dados
    private final SQLExecutor executor;

    /**
     * Cria a tabela "home_users" no banco de dados, se ela não existir.
     * Envia uma mensagem para o console do Bukkit indicando a criação da tabela.
     */
    public void createTable(){
        Bukkit.getConsoleSender().sendMessage("§2Criando tabela...");
        executor.updateQuery(CREATE_TABLE_QUERY);
    }

    /**
     * Insere um novo registro na tabela "home_users".
     *
     * @param user o objeto UserHome a ser inserido
     */
    public void insert(@NotNull UserHome user) {
        String homesJson = GSON.toJson(user.getHomeList());
        CompletableFuture.runAsync(() -> executor.updateQuery(INSERT_ONE_QUERY, stat -> {
            stat.set(1, user.getUuid().toString());
            stat.set(2, user.getName());
            stat.set(3, homesJson);
        }));
    }

    /**
     * Atualiza um registro existente na tabela "home_users".
     *
     * @param user o objeto UserHome a ser atualizado
     */
    public void update(@NotNull UserHome user) {
        String homesJson = GSON.toJson(user.getHomeList());
        CompletableFuture.runAsync(() -> executor.updateQuery(UPDATE_ONE_QUERY, stat -> {
            stat.set(1, homesJson);
            stat.set(2, user.getName());
            stat.set(3, user.getUuid().toString());
        }));
    }

    /**
     * Seleciona um registro da tabela "home_users" pelo UUID.
     *
     * @param uuid o UUID do usuário a ser selecionado
     * @return o objeto UserHome correspondente ao UUID, ou null se não encontrado
     */
    public UserHome select(@NotNull UUID uuid) {
        UserHome user = executor.resultOneQuery(SELECT_ONE_QUERY, stat -> stat.set(1, uuid.toString()), SQLAdapter.class);
        return user;
    }
}
