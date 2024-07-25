package com.carlos.plugins.tst.data;

import com.carlos.plugins.tst.model.UserHome;
import com.google.common.cache.Cache;
import lombok.experimental.Delegate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Classe de armazenamento de memoria do servidor, onde fica os player e sua home, durante o funcionamento do servidor
 */
public class UserCache {

    @Delegate
    private final Map<UUID, UserHome> playersMap;

    public UserCache(){
        this.playersMap = new HashMap<>();
    }
}
