package school.sptech;

import school.sptech.Reader.LeitorNotas;
import school.sptech.Reader.LeitorQuestoes;
import school.sptech.dao.HabilidadeDao;
import school.sptech.dao.NotaMunicipalDao;
import school.sptech.dao.QuestaoDao;
import school.sptech.model.NotaMunicipal;
import school.sptech.model.Questao;

import java.util.List;

import static school.sptech.Log.info;
import static school.sptech.TabelasBanco.tabelasBanco;

public class Main {
    public static void main(String[] args) {

        tabelasBanco();

        String caminhoHabilidades   = "src\\main\\resources\\matriz_referencia_enem.xlsx";
        String caminhoQuestoes      = "src\\main\\resources\\questoesEnem.xlsx";
        String caminhoNotasMunicipio = "src\\main\\resources\\municipioDeSaoPauloResutadosEnem.xlsx";

        // ── Habilidades ──────────────────────────────────────────
        info("--- Carregando Habilidades ---");
        LeitorQuestoes leitor = new LeitorQuestoes(caminhoHabilidades);

        HabilidadeDao habilidadeDao = new HabilidadeDao();
        habilidadeDao.inserirAreaConhecimento();
        habilidadeDao.inserirTodos(leitor.getHabilidades());

        // ── Questões ─────────────────────────────────────────────
        info("--- Carregando Questoes ---");



       leitor.lerArquivo(caminhoQuestoes);

        List<Questao> questoes = leitor.getQuestoes();



        QuestaoDao questaoDao = new QuestaoDao();
        questaoDao.inserirTodos(questoes);

        // ── Notas Municipais ──────────────────────────────────────
        info("--- Carregando Notas ---");
        LeitorNotas leitorNotas = new LeitorNotas();
        leitorNotas.lerArquivo(caminhoNotasMunicipio);

        NotaMunicipal nota = leitorNotas.calcularMediaTemas();

        NotaMunicipalDao notaMunicipalDao = new NotaMunicipalDao();
        notaMunicipalDao.inserir(nota);

    }
}