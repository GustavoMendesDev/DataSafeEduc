package school.sptech;

import school.sptech.Reader.LeitorNotas;
import school.sptech.Reader.LeitorQuestoes;
import school.sptech.dao.HabilidadeDao;
import school.sptech.dao.NotaMunicipalDao;
import school.sptech.dao.QuestaoDao;
import school.sptech.model.NotaMunicipal;
import school.sptech.model.Questao;

<<<<<<< HEAD
=======
import java.util.ArrayList;
>>>>>>> dashboard
import java.util.List;

import static school.sptech.Log.info;
import static school.sptech.TabelasBanco.tabelasBanco;

public class Main {
    public static void main(String[] args) {

        tabelasBanco();

<<<<<<< HEAD
//        String caminhoHabilidades    = "src\\main\\resources\\matriz_referencia_enem.xlsx";
//        String caminhoQuestoes       = "src\\main\\resources\\questoesEnem.xlsx";
//        String caminhoNotasMunicipio = "src\\main\\resources\\municipioDeSaoPauloResutadosEnem.xlsx";


        String caminhoHabilidades    = "matriz_referencia_enem.xlsx";
        String caminhoQuestoes       = "questoesEnem.xlsx";
        String caminhoNotasMunicipio = "municipioDeSaoPauloResultadosEnem.xlsx";



        // ── 1. Habilidades ────────────────────────────────────────
        info("--- Carregando Habilidades ---");
        LeitorQuestoes leitor = new LeitorQuestoes(caminhoHabilidades); // lê habilidades no construtor

        HabilidadeDao habilidadeDao = new HabilidadeDao();
        habilidadeDao.inserirAreaConhecimento();
        habilidadeDao.inserirTodos(leitor.getHabilidades());

        // ── 2. Questões ───────────────────────────────────────────
        info("--- Carregando Questoes ---");
        leitor.lerArquivo(caminhoQuestoes); // lê questões usando habilidades já carregadas

        List<Questao> questoes = leitor.getQuestoes();

        QuestaoDao questaoDao = new QuestaoDao();
        questaoDao.inserirTodos(questoes);
=======
        String caminhoHabilidades        = "matriz_referencia_enem.xlsx";
        String caminhoQuestoes           = "questoesEnem.xlsx";
        String caminhoQuestoes2023       = "questoesEnem2023.xlsx";
        String caminhoQuestoes2022       = "questoesEnem2022.xlsx";
        String caminhoQuestoes2021       = "questoesEnem2021.xlsx";
        String caminhoQuestoes2020       = "questoesEnem2020.xlsx";
        String caminhoNotasMunicipio     = "municipioDeSaoPauloResultadosEnem.xlsx";
        String caminhoNotasMunicipio2023 = "municipioDeSaoPauloResultadosEnem2023.xlsx";
        String caminhoNotasMunicipio2022 = "municipioDeSaoPauloResultadosEnem2022.xlsx";
        String caminhoNotasMunicipio2021 = "municipioDeSaoPauloResultadosEnem2021.xlsx";
        String caminhoNotasMunicipio2020 = "municipioDeSaoPauloResultadosEnem2020.xlsx";

        // ── 1. Habilidades ────────────────────────────────────────
        info("--- Carregando Habilidades ---");
        LeitorQuestoes leitor = new LeitorQuestoes(caminhoHabilidades);

        HabilidadeDao habilidadeDao = new HabilidadeDao();
        habilidadeDao.inserirAreaConhecimento();
        habilidadeDao.inserirTodos(leitor.getHabilidades());

        // ── 2. Questões ───────────────────────────────────────────
        info("--- Carregando Questoes ---");
        QuestaoDao questaoDao = new QuestaoDao();

        leitor.lerArquivo(caminhoQuestoes);
        questaoDao.inserirQuestoes(leitor.getQuestoes(), 2024);

        leitor.lerArquivo(caminhoQuestoes2023);
        questaoDao.inserirQuestoes(leitor.getQuestoes(), 2023);

        leitor.lerArquivo(caminhoQuestoes2022);
        questaoDao.inserirQuestoes(leitor.getQuestoes(), 2022);

        leitor.lerArquivo(caminhoQuestoes2021);
        questaoDao.inserirQuestoes(leitor.getQuestoes(), 2021);

        leitor.lerArquivo(caminhoQuestoes2020);
        questaoDao.inserirQuestoes(leitor.getQuestoes(), 2020);
>>>>>>> dashboard

        // ── 3. Notas Municipais ───────────────────────────────────
        info("--- Carregando Notas ---");
        LeitorNotas leitorNotas = new LeitorNotas();
<<<<<<< HEAD
        leitorNotas.lerArquivo(caminhoNotasMunicipio);

        NotaMunicipal nota = leitorNotas.calcularMediaTemas();

        NotaMunicipalDao notaMunicipalDao = new NotaMunicipalDao();
        notaMunicipalDao.inserir(nota);
=======
        NotaMunicipalDao notaMunicipalDao = new NotaMunicipalDao();

        List<NotaMunicipal> todasAsNotas = new ArrayList<>();

        leitorNotas.lerArquivo(caminhoNotasMunicipio);
        todasAsNotas.add(leitorNotas.calcularMediaTemas());

        leitorNotas.lerArquivo(caminhoNotasMunicipio2023);
        todasAsNotas.add(leitorNotas.calcularMediaTemas());

        leitorNotas.lerArquivo(caminhoNotasMunicipio2022);
        todasAsNotas.add(leitorNotas.calcularMediaTemas());

        leitorNotas.lerArquivo(caminhoNotasMunicipio2021);
        todasAsNotas.add(leitorNotas.calcularMediaTemas());

        leitorNotas.lerArquivo(caminhoNotasMunicipio2020);
        todasAsNotas.add(leitorNotas.calcularMediaTemas());

        notaMunicipalDao.inserirTodos(todasAsNotas);
>>>>>>> dashboard

    }
}