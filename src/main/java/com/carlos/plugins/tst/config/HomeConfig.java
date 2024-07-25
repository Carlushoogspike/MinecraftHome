package com.carlos.plugins.tst.config;

import lombok.Getter;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

/**
 * Classe de configurações do servidor
 */
@Getter
public class HomeConfig {

    private boolean cooldown;
    private int cooldownSeconds;
    private boolean cancelToMove;
    private boolean opIgnorePerms;

    private boolean particle;
    private Particle particleType;
    private String particleColor;
    private int particleAmount;

    //realiza o trabalho de entender a config.yml e transmitir para codigo
    public HomeConfig(FileConfiguration config) {
        opIgnorePerms = config.getBoolean("op-ignore-perms");
        if (config.contains("cooldown")) {
            cooldown = config.getBoolean("cooldown.enabled");
            cooldownSeconds = config.getInt("cooldown.time");
            cancelToMove = config.getBoolean("cooldown.cancel-to-move");
        }

        particle = config.getBoolean("particle.enabled");
        if (particle) {
            particleType = translate(config.getString("particle.key"));
            particleColor = config.getString("particle.color");
            particleAmount = config.getInt("particle.amount");
        }
    }

    /**
     * Traduz uma string para um valor de Particle correspondente.
     *
     * @param name O nome da partícula em string.
     * @return A partícula correspondente ou null se não for encontrada.
     */
    private Particle translate(String name) {
        return Arrays.stream(Particle.values())
                .filter(a -> a.name().equalsIgnoreCase(name))
                .findAny()
                .orElse(null);
    }

}
