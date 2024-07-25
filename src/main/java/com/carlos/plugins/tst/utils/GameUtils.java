package com.carlos.plugins.tst.utils;

import com.carlos.plugins.tst.config.HomeConfig;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * A classe GameUtils contém utilitários relacionados ao jogo, como a geração de partículas.
 */
public class GameUtils {

    /**
     * Gera partículas ao redor do jogador com base nas configurações fornecidas.
     *
     * @param config A configuração que contém os parâmetros para gerar as partículas.
     * @param player O jogador ao redor do qual as partículas serão geradas.
     */
    public static void particle(HomeConfig config, Player player) {
        // Verifica se a geração de partículas está habilitada na configuração
        if (!config.isParticle()) return;

        // Obtém o tipo de partícula, quantidade e cor das configurações
        Particle type = config.getParticleType();
        int amount = config.getParticleAmount();
        String colorHex = config.getParticleColor();

        // Obtém a localização atual do jogador
        Location loc = player.getLocation();

        // Clona a localização e ajusta a altura para gerar partículas acima do jogador
        Location cloned = loc.clone().add(0, 1, 0);
        for (int i = 0; i < amount; i++) {
            // Gera offsets aleatórios para a posição das partículas
            double offsetX = Math.random() * 3 - 1;
            double offsetY = Math.random() * 3 - 1;
            double offsetZ = Math.random() * 3 - 1;

            // Calcula a posição final da partícula
            Location particleLocation = cloned.clone().add(offsetX, offsetY, offsetZ);

            // Se o tipo de partícula for DUST, configura a cor da partícula
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
