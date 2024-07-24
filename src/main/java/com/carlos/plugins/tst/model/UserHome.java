package com.carlos.plugins.tst.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;
import org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.UUID;

@Data @Builder
public class UserHome {

    private String name;
    private UUID uuid;
    private Location lastDied;
    private List<Home> homeList;

    private boolean dirty = false;
}
