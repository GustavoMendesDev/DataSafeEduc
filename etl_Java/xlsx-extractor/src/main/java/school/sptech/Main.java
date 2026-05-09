package school.sptech;

import school.sptech.dao.LeitorExcelQuestaoDao;
import school.sptech.dao.LeitorExcelResultadoDao;
import static school.sptech.Log.*;

import static school.sptech.TabelasBanco.tabelasBanco;


public class Main {
    public static void main(String[] args)  {

        tabelasBanco();


// caminho raiz (\DataSafeEduc\etl_Java\xlsx-extractor> )
// 1. Defina os caminhos dos arquivos
        String caminhoHabilidades = "src\\main\\resources\\matriz_referencia_enem.xlsx";
        String caminhoQuestoes = "src\\main\\resources\\questoesEnem.xlsx";
        String caminhoNotasMunicipio = "src\\main\\resources\\municipioDeSaoPauloResutadosEnem.xlsx";

        // 2. Crie APENAS UM leitor
        LeitorExcelQuestaoDao leitor = new LeitorExcelQuestaoDao();

        // 3. Use o mesmo leitor para carregar as habilidades primeiro
        info("--- Carregando Habilidades ---");
        leitor.lerHabilidades(caminhoHabilidades);
//        leitor.mostrarHabilidades();

        // 4. Use o MESMO leitor para ler as questões
        System.out.println("\n");
        info("--- Carregando Questões ---");
        leitor.lerQuestoes(caminhoQuestoes);
//        leitor.mostrarQuestoes();

        LeitorExcelResultadoDao leitorNotas = new LeitorExcelResultadoDao();
        System.out.println("\n");
        info("--- Carregando Notas ---");
        leitorNotas.extrairExcelResultado(caminhoNotasMunicipio);
        leitorNotas.calcularMediaTemas();


    }
}