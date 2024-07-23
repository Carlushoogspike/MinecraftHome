package sqlmodule.connector.type.impl;

import lombok.Builder;
import sqlmodule.connector.type.FileType;
import sqlmodule.utils.DataFileUtils;

import java.io.File;

/**
 * Implementação concreta da classe {@link FileType} para bancos de dados SQLite.
 * Esta classe configura o tipo de conexão específico para SQLite, utilizando o driver JDBC e a URL apropriados.
 */
public class LiteSQLType extends FileType {

    /**
     * Construtor que configura o tipo de conexão para SQLite.
     *
     * @param file Arquivo do banco de dados SQLite.
     */
    @Builder
    public LiteSQLType(File file) {
        super(
                "org.sqlite.JDBC", // Nome da classe do driver JDBC para SQLite
                "jdbc:sqlite:" + file.getAbsolutePath(), // URL JDBC para a conexão com o banco de dados
                file // Arquivo do banco de dados
        );
        // Cria o arquivo do banco de dados se ele não existir
        DataFileUtils.createFile(file);
    }
}
