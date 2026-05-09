// dao/NotaMunicipalDao.java
package school.sptech.dao;

import school.sptech.ConexaoBanco;
import school.sptech.model.NotaMunicipal;

import java.util.List;

import static school.sptech.Log.erro;
import static school.sptech.Log.info;

public class NotaMunicipalDao {

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