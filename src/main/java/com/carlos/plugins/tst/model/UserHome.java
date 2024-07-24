package com.carlos.plugins.tst.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class UserHome {

    private String name;
    private UUID uuid;

    @Builder.Default
    private Map<String, Home> homeList = new HashMap<>();

    @Builder.Default
    private boolean dirty = false;

    public boolean containsHome(String name) {
        return homeList.containsKey(name);
    }

    public boolean existHomeInLocation(Location loc) {
        return homeList.values().stream()
                .anyMatch(home -> home.getLocation().equals(loc));
    }

    public void removeHome(String name) {
        homeList.remove(name);
    }

    public Home getHome(String name) {
        return homeList.get(name);
    }
}
