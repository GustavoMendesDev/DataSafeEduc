package school.sptech;

import school.sptech.dao.LeitorExcelQuestaoDao;
import school.sptech.dao.LeitorExcelResultadoDao;

import static school.sptech.TabelasBanco.tabelasBanco;


public class Main {
    public static void main(String[] args)  {

        tabelasBanco();


// caminho raiz (\DataSafeEduc\etl_Java\xlsx-extractor> )
// 1. Defina os caminhos dos arquivos
        String caminhoHabilidades = "matriz_referencia_enem.xlsx";
        String caminhoQuestoes = "questoesEnem.xlsx";
        String caminhoNotasMunicipio = "municipioDeSaoPauloResutadosEnem.xlsx";

        // 2. Crie APENAS UM leitor
        LeitorExcelQuestaoDao leitor = new LeitorExcelQuestaoDao();

        // 3. Use o mesmo leitor para carregar as habilidades primeiro
        System.out.println("--- Carregando Habilidades ---");
        leitor.lerHabilidades(caminhoHabilidades);
//        leitor.mostrarHabilidades();

        // 4. Use o MESMO leitor para ler as questões
        System.out.println("\n--- Carregando Questões ---");
        leitor.lerQuestoes(caminhoQuestoes);
//        leitor.mostrarQuestoes();

        LeitorExcelResultadoDao leitorNotas = new LeitorExcelResultadoDao();

        System.out.println("\n--- Carregando Notas ---");
        leitorNotas.extrairExcelResultado(caminhoNotasMunicipio);
        leitorNotas.calcularMediaTemas();


    }
}