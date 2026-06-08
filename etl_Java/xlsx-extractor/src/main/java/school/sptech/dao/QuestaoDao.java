// dao/QuestaoDao.java
package school.sptech.dao;

import school.sptech.ConexaoBanco;
import school.sptech.model.Questao;
import org.apache.commons.dbcp2.BasicDataSource;
import school.sptech.ConexaoBanco;
import school.sptech.model.Questao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    private static Integer contador = 0;


        public void inserirQuestoes(List<Questao> questoes, int anoExame) {

            String url = "jdbc:mysql://localhost:3306/datasafe?useSSL=true&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true";
            String user = "root";
            String password = "#0612@Gm";

            String sqlParametroTri = "INSERT IGNORE INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (?, ?, ?, ?, ?)";
            String sqlQuestao      = "INSERT IGNORE INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES (?, ?, ?, ?)";

            try (Connection conexao = DriverManager.getConnection(url, user, password);
                 PreparedStatement psParametroTri = conexao.prepareStatement(sqlParametroTri);
                 PreparedStatement psQuestao      = conexao.prepareStatement(sqlQuestao)) {

                conexao.setAutoCommit(false);

                int count = 0;

                for (Questao questao : questoes) {
                    if (questao == null || questao.getDificuldade() == null) continue;
                    contador++;

                    psParametroTri.setInt(1, questao.getDificuldade().getId());
                    psParametroTri.setString(2, questao.getDificuldade().calcularDificuldade(questao.getDificuldade().getParametro_b()));
                    psParametroTri.setDouble(3, questao.getDificuldade().getParametro_a());
                    psParametroTri.setDouble(4, questao.getDificuldade().getParametro_b());
                    psParametroTri.setDouble(5, questao.getDificuldade().getParametro_c());
                    psParametroTri.addBatch();

                    psQuestao.setInt(1, questao.getCodigoItem());
                    psQuestao.setInt(2, anoExame);
                    psQuestao.setInt(3, questao.getHabilidade().getId());
                    psQuestao.setInt(4, questao.getDificuldade().getId());
                    psQuestao.addBatch();

                    count++;

                    if (count % 500 == 0) {
                        psParametroTri.executeBatch();
                        psQuestao.executeBatch();
                        psParametroTri.clearBatch();
                        psQuestao.clearBatch();
                    }
                }

                // Restante que não fechou um chunk de 500
                psParametroTri.executeBatch();
                psQuestao.executeBatch();

                conexao.commit();
                info("(QuestaoDao) - " + questoes.size() + " questões inseridas com sucesso! Ano: " + anoExame);

            } catch (Exception e) {
                erro("(QuestaoDao) - Falha ao inserir questões do ano " + anoExame + ": " + e.getMessage());
            }
        }
    }

