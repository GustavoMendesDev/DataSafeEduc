// dao/NotaMunicipalDao.java
package school.sptech.dao;

import school.sptech.ConexaoBanco;
import school.sptech.model.NotaMunicipal;

<<<<<<< HEAD
=======
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
>>>>>>> dashboard
import java.util.List;

import static school.sptech.Log.erro;
import static school.sptech.Log.info;

public class NotaMunicipalDao {

<<<<<<< HEAD
    public void inserir(NotaMunicipal notaMunicipal) {
        try {
            ConexaoBanco.CONEXAO.update(
                    "INSERT INTO notaMunicipal (matematica, codigosELinguagens, cienciasDaNatureza, cienciasHumanas) VALUES (?, ?, ?, ?)",
                    notaMunicipal.getMediaMatematica(),
                    notaMunicipal.getMediaCodigosLinguagens(),
                    notaMunicipal.getMediaCienciasNatureza(),
                    notaMunicipal.getMediaCienciasHumanas()
            );
            info("(NotaMunicipalDao) - Nota municipal inserida com sucesso! " +
                    "MT: %.2f | LC: %.2f | CN: %.2f | CH: %.2f".formatted(
                            notaMunicipal.getMediaMatematica(),
                            notaMunicipal.getMediaCodigosLinguagens(),
                            notaMunicipal.getMediaCienciasNatureza(),
                            notaMunicipal.getMediaCienciasHumanas()
                    ));

        } catch (Exception e) {
            erro("(NotaMunicipalDao) - Falha ao inserir nota municipal: " + e.getMessage());
        }
    }

    public void inserirTodos(List<NotaMunicipal> notas) {
        info("(NotaMunicipalDao) - Iniciando inserção de " + notas.size() + " notas municipais...");

        for (NotaMunicipal nota : notas) {
            inserir(nota);
        }

        info("(NotaMunicipalDao) - Inserção de notas municipais finalizada!");
    }
}
=======
    private static final String URL = "jdbc:mysql://localhost:3306/datasafe?useSSL=true&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "#0612@Gm";

    public void inserirTodos(List<NotaMunicipal> notas) {
        String sql = "INSERT INTO notaMunicipal (matematica, codigosELinguagens, cienciasDaNatureza, cienciasHumanas) VALUES (?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            conexao.setAutoCommit(false);

            int count = 0;

            for (NotaMunicipal nota : notas) {
                if (nota == null) continue;

                ps.setDouble(1, nota.getMediaMatematica());
                ps.setDouble(2, nota.getMediaCodigosLinguagens());
                ps.setDouble(3, nota.getMediaCienciasNatureza());
                ps.setDouble(4, nota.getMediaCienciasHumanas());
                ps.addBatch();

                count++;

                if (count % 500 == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }

            ps.executeBatch();
            conexao.commit();

            info("(NotaMunicipalDao) - " + notas.size() + " notas municipais inseridas com sucesso!");

        } catch (Exception e) {
            erro("(NotaMunicipalDao) - Falha ao inserir notas municipais: " + e.getMessage());
        }
    }
}
>>>>>>> dashboard
