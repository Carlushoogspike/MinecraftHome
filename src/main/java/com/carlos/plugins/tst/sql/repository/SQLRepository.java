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

@RequiredArgsConstructor
public class SQLRepository {

    private final SQLExecutor executor;

    public void createTable(){
        Bukkit.getConsoleSender().sendMessage("§2Criando tabela...");
        executor.updateQuery(CREATE_TABLE_QUERY);
    }

    public void insert(@NotNull UserHome user){
        Bukkit.getConsoleSender().sendMessage("§8Inserindo jogador na tabela...");
        CompletableFuture.runAsync(() -> executor.updateQuery(INSERT_ONE_QUERY, stat ->{
            stat.set(1, user.getUuid());
            stat.set(2, user.getName());
            stat.set(3, user.getLastDied());
            stat.set(4, GSON.toJson(user.getHomeList()));
        }));
    }

    public void update(@NotNull UserHome user){
        Bukkit.getConsoleSender().sendMessage("§8Atualizando jogador na tabela...");
        CompletableFuture.runAsync(() -> executor.updateQuery(UPDATE_ONE_QUERY, stat ->{
            stat.set(1, user.getHomeList());
            stat.set(2, user.getLastDied());
            stat.set(3, user.getName());
            stat.set(4, user.getUuid());
        }));
    }

    public UserHome select(@NotNull UUID uuid){
        return executor.resultOneQuery(SELECT_ONE_QUERY, stat -> stat.set(1, uuid), SQLAdapter.class);
    }
}
