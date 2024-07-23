package com.carlos.plugins.tst.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;

@AllArgsConstructor
@Builder
@Data
public class Home {

    private String name;
    private Location location;
    private boolean openPublic;

}
