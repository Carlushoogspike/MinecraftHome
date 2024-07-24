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

    public void insert(@NotNull UserHome user) {
        String homesJson = GSON.toJson(user.getHomeList());
        CompletableFuture.runAsync(() -> executor.updateQuery(INSERT_ONE_QUERY, stat -> {
            stat.set(1, user.getUuid().toString());
            stat.set(2, user.getName());
            stat.set(3, homesJson);
        }));
    }

    public void update(@NotNull UserHome user) {
        String homesJson = GSON.toJson(user.getHomeList());
        CompletableFuture.runAsync(() -> executor.updateQuery(UPDATE_ONE_QUERY, stat -> {
            stat.set(1, homesJson);
            stat.set(2, user.getName());
            stat.set(3, user.getUuid().toString());
        }));
    }


    public UserHome select(@NotNull UUID uuid) {
        UserHome user = executor.resultOneQuery(SELECT_ONE_QUERY, stat -> stat.set(1, uuid.toString()), SQLAdapter.class);
        return user;
    }


}
