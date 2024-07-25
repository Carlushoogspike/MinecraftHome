package com.carlos.plugins.tst.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Classe de Modelo UserHOme
 */
@Data
@Builder
public class UserHome {

    private String name; //nome do seu usuario
    private UUID uuid; //id do player

    @Builder.Default
    private Map<String, Home> homeList = new HashMap<>(); //mapa de chave e valor onde , chave = nome da home, valor = home

    @Builder.Default
    private boolean dirty = false; //um atributo utilizado em banco de dados para verificar alterações

    //se contem a home
    public boolean containsHome(String name) {
        return homeList.containsKey(name);
    }

    //se existe uma home naquele lugar
    public boolean existHomeInLocation(Location loc) {
        return homeList.values().stream()
                .anyMatch(home -> home.getLocation().equals(loc));
    }

    //remove a home
    public void removeHome(String name) {
        homeList.remove(name);
    }

    //obtem a home
    public Home getHome(String name) {
        return homeList.get(name);
    }
}
