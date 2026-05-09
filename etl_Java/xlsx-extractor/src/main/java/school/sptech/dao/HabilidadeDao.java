// dao/HabilidadeDao.java
package school.sptech.dao;

import school.sptech.ConexaoBanco;
import school.sptech.model.Habilidade;

import java.util.List;

import static school.sptech.Log.info;
import static school.sptech.Log.erro;

public class HabilidadeDao {

    public void inserirAreaConhecimento() {
        try {
            ConexaoBanco.CONEXAO.update("INSERT INTO areaConhecimento (id, nome, sigla) VALUES (?, ?, ?)",
                    1, "Linguagens e Códigos", "LC");
            info("(HabilidadeDao) - Área LC inserida com sucesso!");

            ConexaoBanco.CONEXAO.update("INSERT INTO areaConhecimento (id, nome, sigla) VALUES (?, ?, ?)",
                    2, "Matematica", "MT");
            info("(HabilidadeDao) - Área MT inserida com sucesso!");

            ConexaoBanco.CONEXAO.update("INSERT INTO areaConhecimento (id, nome, sigla) VALUES (?, ?, ?)",
                    3, "Ciências da Natureza", "CN");
            info("(HabilidadeDao) - Área CN inserida com sucesso!");

            ConexaoBanco.CONEXAO.update("INSERT INTO areaConhecimento (id, nome, sigla) VALUES (?, ?, ?)",
                    4, "Ciências Humanas", "CH");
            info("(HabilidadeDao) - Área CH inserida com sucesso!");

        } catch (Exception e) {
            erro("(HabilidadeDao) - Falha ao inserir áreas de conhecimento: " + e.getMessage());
        }
    }

    public void inserir(Habilidade habilidade) {
        try {
            ConexaoBanco.CONEXAO.update(
                    "INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (?, ?, ?, ?)",
                    habilidade.getId(),
                    habilidade.getNumero(),
                    habilidade.getDescricao(),
                    habilidade.getSigla().getCodigo()
            );
            info("(HabilidadeDao) - Habilidade " + habilidade.getNumero() + " [" + habilidade.getSigla() + "] inserida com sucesso!");

        } catch (Exception e) {
            erro("(HabilidadeDao) - Falha ao inserir habilidade " + habilidade.getNumero() + ": " + e.getMessage());
        }
    }

    public void inserirTodos(List<Habilidade> habilidades) {
        info("(HabilidadeDao) - Iniciando inserção de " + habilidades.size() + " habilidades...");

        for (Habilidade habilidade : habilidades) {
            inserir(habilidade);
        }

        info("(HabilidadeDao) - Inserção de habilidades finalizada!");
    }
}