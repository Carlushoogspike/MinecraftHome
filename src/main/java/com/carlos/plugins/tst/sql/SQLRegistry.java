package com.carlos.plugins.tst.sql;

import com.carlos.plugins.tst.HomePlugin;
import com.carlos.plugins.tst.model.UserHome;
import com.carlos.plugins.tst.sql.controller.SQLController;
import com.carlos.plugins.tst.sql.repository.DataProvider;
import com.carlos.plugins.tst.sql.repository.SQLRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sqlmodule.connector.SQLConnector;
import sqlmodule.executor.SQLExecutor;

/**
 * A classe SQLRegistry é responsável por registrar eventos do Bukkit e gerenciar o ciclo de vida do controlador SQL.
 * Implementa Listener para escutar eventos de entrada e saída de jogadores.
 */
@RequiredArgsConstructor
public class SQLRegistry implements Listener {

    // Referência ao plugin principal
    private final HomePlugin plugin;

    // Controlador SQL, gerenciado com nível de acesso público
    @Getter(AccessLevel.PUBLIC)
    private SQLController controller;

    /**
     * Inicializa o conector SQL, executor e repositório, cria a tabela se não existir
     * e registra os eventos necessários.
     */
    public void onCreate(){
        SQLConnector connector = new DataProvider(plugin, "users").setup(null);
        SQLExecutor executor = new SQLExecutor(connector);

        SQLRepository repository = new SQLRepository(executor);
        repository.createTable();

        controller = new SQLController(repository, plugin.getCache());

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Atualiza todas as entradas de usuário no cache quando o plugin é desativado.
     */
    public void onDestroy(){
        controller.updateAll();
    }

    /**
     * Evento chamado quando um jogador entra no servidor. Carrega os dados do jogador.
     *
     * @param event o evento de entrada do jogador
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        final Player player = event.getPlayer();
        this.controller.load(player);
    }

    /**
     * Evento chamado quando um jogador sai do servidor. Atualiza os dados do jogador
     * se houverem modificações e remove o jogador do cache.
     *
     * @param event o evento de saída do jogador
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        final Player player = event.getPlayer();
        final UserHome user = plugin.getCache().get(player.getUniqueId());

        if (user == null) return;
        if (!user.isDirty()) return;

        this.controller.update(user);
        plugin.getCache().remove(user.getUuid());
    }
}
