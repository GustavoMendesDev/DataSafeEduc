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







        String caminhoHabilidades    = "matriz_referencia_enem.xlsx";

        String caminhoQuestoes     = "questoesEnem.xlsx";
        String caminhoQuestoes2023 = "questoesEnem2023.xlsx";
        String caminhoQuestoes2022 = "questoesEnem2022.xlsx";
        String caminhoQuestoes2021 = "questoesEnem2021.xlsx";
        String caminhoQuestoes2020 = "questoesEnem2020.xlsx";

         String caminhoNotasMunicipio = "municipioDeSaoPauloResultadosEnem.xlsx";

//        String caminhoHabilidades    = "src/main/resources/matriz_referencia_enem.xlsx";
//        String caminhoQuestoes       = "src/main/resources/questoesEnem.xlsx";
//        String caminhoNotasMunicipio = "src/main/resources/municipioDeSaoPauloResultadosEnem.xlsx";

        // ── 1. Habilidades ────────────────────────────────────────
        info("--- Carregando Habilidades ---");
        LeitorQuestoes leitor = new LeitorQuestoes(caminhoHabilidades);

        HabilidadeDao habilidadeDao = new HabilidadeDao();
        habilidadeDao.inserirAreaConhecimento();
        habilidadeDao.inserirTodos(leitor.getHabilidades());

// ── 2. Questões ───────────────────────────────────────────
        info("--- Carregando Questoes ---");

        leitor.lerArquivo(caminhoQuestoes);
       leitor.lerArquivo(caminhoQuestoes2023);
        leitor.lerArquivo(caminhoQuestoes2022);
        leitor.lerArquivo(caminhoQuestoes2021);
        leitor.lerArquivo(caminhoQuestoes2020);

        List<Questao> questoes = leitor.getQuestoes();

        QuestaoDao questaoDao = new QuestaoDao();
        questaoDao.inserirTodos(questoes);

        // ── 3. Notas Municipais ───────────────────────────────────
        info("--- Carregando Notas ---");
        LeitorNotas leitorNotas = new LeitorNotas();
        leitorNotas.lerArquivo(caminhoNotasMunicipio);

        NotaMunicipal nota = leitorNotas.calcularMediaTemas();

        NotaMunicipalDao notaMunicipalDao = new NotaMunicipalDao();
        notaMunicipalDao.inserir(nota);

    }
}