package com.carlos.plugins.tst.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data @Builder
public class UserHome {

    private String name;
    private UUID uuid;
    private Location lastDied;

    @Builder.Default
    private List<Home> homeList = new ArrayList<>();

    @Builder.Default
    private boolean dirty = false;

    public boolean containsHome(String name){
        return homeList.stream()
                .anyMatch(home -> home.getName().equals(name));
    }

    public boolean existHomeInLocation(Location loc){
        return homeList.stream()
                .anyMatch(home -> home.getLocation().equals(loc));
    }

    public void removeHome(String name){
        homeList.removeIf(home -> home.getName().equals(name));
    }

    public Home getHome(String name){
        return homeList.stream()
                .filter(h -> h.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
