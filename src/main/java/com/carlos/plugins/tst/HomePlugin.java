package com.carlos.plugins.tst;

import com.carlos.plugins.tst.command.HomeCommands;
import com.carlos.plugins.tst.config.CooldownManager;
import com.carlos.plugins.tst.config.HomeConfig;
import com.carlos.plugins.tst.data.UserCache;
import com.carlos.plugins.tst.listener.HomeListener;
import com.carlos.plugins.tst.sql.SQLRegistry;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

/**
 * A classe principal do plugin HomePlugin, responsável por inicializar e gerenciar o ciclo de vida do plugin.
 */
@Getter
public class HomePlugin extends JavaPlugin {

    @Getter
    private static HomePlugin instance;

    private HomeConfig homeConfig;
    private CooldownManager cooldownManager;
    private UserCache cache;
    private SQLRegistry sql;

    /**
     * Método chamado quando o plugin é carregado.
     * Salva a configuração padrão do plugin.
     */
    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    /**
     * Método chamado quando o plugin é habilitado.
     * Inicializa as várias componentes do plugin, como o cache de usuários, configurações, comandos e eventos.
     */
    @Override
    public void onEnable() {
        instance = this;

        // Inicializa o cache de usuários
        cache = new UserCache();

        // Configura e inicializa o registro SQL
        sql = new SQLRegistry(this);
        sql.onCreate();

        // Carrega as configurações do plugin
        homeConfig = new HomeConfig(getConfig());

        // Inicializa o gerenciador de cooldown
        cooldownManager = new CooldownManager(this);

        // Registra os comandos do plugin
        Arrays.asList("home", "phome", "sethome", "delhome", "setpublic", "setprivate").forEach(c -> {
            getCommand(c).setExecutor(new HomeCommands(this));
            getCommand(c).setTabCompleter(new HomeCommands(this));
        });

        // Registra os listeners de eventos
        getServer().getPluginManager().registerEvents(new HomeListener(cooldownManager, homeConfig), this);
    }

    /**
     * Método chamado quando o plugin é desabilitado.
     * Executa a rotina de destruição do SQLRegistry, garantindo que todas as mudanças sejam salvas.
     */
    @Override
    public void onDisable() {
        sql.onDestroy();
    }
}
