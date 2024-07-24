package com.carlos.plugins.tst.sql.controller;

import com.carlos.plugins.tst.data.UserCache;
import com.carlos.plugins.tst.model.UserHome;
import com.carlos.plugins.tst.sql.repository.SQLRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SQLController {

    private final SQLRepository repository;
    private final UserCache cache;

    public void load(Player player){
        UserHome user = repository.select(player.getUniqueId());

        if (user == null){
            user = createBlank(player);
            repository.insert(user);
        }

        cache.put(user.getUuid(), user);
    }

    public void update(UserHome userHome){
        repository.update(userHome);
        userHome.setDirty(true);
    }

    public void updateAll(){
        for (UserHome h : cache.values()){
            if (!h.isDirty()) continue;
            update(h);
        }
    }

    public UserHome createBlank(Player player){
        return  UserHome.builder().name(player.getName()).uuid(player.getUniqueId()).build();
    }
}
