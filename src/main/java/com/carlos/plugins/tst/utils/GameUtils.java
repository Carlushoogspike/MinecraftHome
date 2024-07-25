package com.carlos.plugins.tst.utils;

import com.carlos.plugins.tst.config.HomeConfig;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class GameUtils {

    public static void particle(HomeConfig config,  Player player){
        if (!config.isParticle()) return;

        Particle type = config.getParticleType();
        int amount = config.getParticleAmount();
        String colorHex = config.getParticleColor();

        Location loc = player.getLocation();

        Location cloned = loc.clone().add(0, 1, 0);
        for (int i = 0; i < amount; i++) {
            double offsetX = Math.random() * 3 - 1;
            double offsetY = Math.random() * 3 - 1;
            double offsetZ = Math.random() * 3 - 1;

            Location particleLocation = cloned.clone().add(offsetX, offsetY, offsetZ);
            if (type == Particle.DUST) {
                java.awt.Color color = hexToColor(colorHex);
                Particle.DustOptions dustOptions = new Particle.DustOptions(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()), 1);

                cloned.getWorld().spawnParticle(type, particleLocation, 0, 0, 0, 0, 5, dustOptions);

            } else {
                cloned.getWorld().spawnParticle(type, particleLocation, 0, 0, 0, 0, 5);
            }
        }
    }

    /**
     * Converte uma cor hexadecimal para uma instância de {@link java.awt.Color}.
     *
     * @param hex A cor em formato hexadecimal.
     * @return A instância de {@link java.awt.Color}.
     */
    private static java.awt.Color hexToColor(String hex) {
        return java.awt.Color.decode("#" + hex);
    }


}
