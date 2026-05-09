// dao/QuestaoDao.java
package school.sptech.dao;

import school.sptech.ConexaoBanco;
import school.sptech.model.Questao;

import java.util.List;

import static school.sptech.Log.erro;
import static school.sptech.Log.info;

public class QuestaoDao {

    public void inserirParametroTri(Questao questao) {
        try {
            ConexaoBanco.CONEXAO.update(
                    "INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (?, ?, ?, ?, ?)",
                    questao.getDificuldade().getId(),
                    questao.getDificuldade().calcularDificuldade(questao.getDificuldade().getParametro_b()),
                    questao.getDificuldade().getParametro_a(),
                    questao.getDificuldade().getParametro_b(),
                    questao.getDificuldade().getParametro_c()
            );
            info("(QuestaoDao) - ParametroTri da questão " + questao.getCodigoItem() + " inserido com sucesso!");

        } catch (Exception e) {
            erro("(QuestaoDao) - Falha ao inserir parametroTri da questão " + questao.getCodigoItem() + ": " + e.getMessage());
        }
    }

    public void inserir(Questao questao) {
        try {
            ConexaoBanco.CONEXAO.update(
                    "INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES (?, ?, ?, ?)",
                    questao.getCodigoItem(),
                    2024,
                    questao.getHabilidade().getId(),
                    questao.getDificuldade().getId()
            );
            info("(QuestaoDao) - Questão " + questao.getCodigoItem() + " inserida com sucesso!");

        } catch (Exception e) {
            erro("(QuestaoDao) - Falha ao inserir questão " + questao.getCodigoItem() + ": " + e.getMessage());
        }
    }

    public void inserirTodos(List<Questao> questoes) {
        info("(QuestaoDao) - Iniciando inserção de " + questoes.size() + " questões...");

        for (Questao questao : questoes) {
            inserirParametroTri(questao);
            inserir(questao);
        }

        info("(QuestaoDao) - Inserção de questões finalizada!");
    }
}