package com.carlos.plugins.tst.sql.controller;

import com.carlos.plugins.tst.data.UserCache;
import com.carlos.plugins.tst.model.UserHome;
import com.carlos.plugins.tst.sql.repository.SQLRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/**
 * A classe SQLController é responsável por gerenciar as operações de carregamento,
 * atualização e criação de dados dos usuários no banco de dados e no cache.
 */
@RequiredArgsConstructor
public class SQLController {

    // Repositório SQL para interagir com o banco de dados
    private final SQLRepository repository;

    // Cache de usuários para armazenar dados em memória
    private final UserCache cache;

    /**
     * Carrega os dados do usuário a partir do banco de dados. Se o usuário não existir no banco,
     * cria um novo registro em branco.
     *
     * @param player o jogador cujo dados serão carregados
     */
    public void load(Player player) {
        // Seleciona o usuário a partir do banco de dados usando o UUID do jogador
        UserHome user = repository.select(player.getUniqueId());

        // Se o usuário não existir no banco de dados, cria um novo registro em branco e insere no banco
        if (user == null) {
            user = createBlank(player);
            repository.insert(user);
        }

        // Adiciona o usuário ao cache
        cache.put(user.getUuid(), user);
    }

    /**
     * Atualiza os dados do usuário no banco de dados e marca o objeto como "sujo".
     *
     * @param userHome o objeto UserHome a ser atualizado
     */
    public void update(UserHome userHome){
        repository.update(userHome);
        userHome.setDirty(true);
    }

    /**
     * Atualiza todos os usuários "sujos" (modificados) no banco de dados.
     */
    public void updateAll(){
        // Percorre todos os usuários no cache
        for (UserHome h : cache.values()){
            // Se o usuário não estiver "sujo", continua para o próximo
            if (!h.isDirty()) continue;
            // Atualiza o usuário
            update(h);
        }
    }

    /**
     * Cria um novo registro em branco para um jogador.
     *
     * @param player o jogador para o qual o registro será criado
     * @return um novo objeto UserHome com os dados do jogador
     */
    public UserHome createBlank(Player player){
        return UserHome.builder()
                .name(player.getName())
                .uuid(player.getUniqueId())
                .build();
    }
}
