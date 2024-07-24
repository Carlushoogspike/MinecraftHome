package com.carlos.plugins.tst.sql.repository;

import com.carlos.plugins.tst.model.UserHome;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import sqlmodule.executor.SQLExecutor;

import java.util.UUID;

@RequiredArgsConstructor
public class SQLRepository {

    private final SQLExecutor executor;

    public void createTable(){

    }

    public void insert(@NotNull UserHome user){

    }

    public void update(@NotNull UserHome user){

    }

    public void select(@NotNull UUID uuid){

    }
}
