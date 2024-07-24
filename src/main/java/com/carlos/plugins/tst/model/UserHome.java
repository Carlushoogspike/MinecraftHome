package com.carlos.plugins.tst.model;

import lombok.AllArgsConstructor;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class UserHome {

    private String name;
    private UUID uuid;
    private Location lastDied;
    private List<Home> homeList;
}
