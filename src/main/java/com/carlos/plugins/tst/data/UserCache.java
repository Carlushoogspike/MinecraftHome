package com.carlos.plugins.tst.data;

import com.carlos.plugins.tst.model.UserHome;
import com.google.common.cache.Cache;
import lombok.experimental.Delegate;

import java.util.HashMap;
import java.util.Map;

public class UserCache {

    @Delegate
    private final Map<String, UserHome> playersMap;

    public UserCache(){
        this.playersMap = new HashMap<>();
    }
}
