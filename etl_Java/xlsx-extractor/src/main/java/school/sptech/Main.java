package school.sptech;

import school.sptech.dao.LeitorExcelQuestao;
import school.sptech.dao.LeitorExcelResultadoDao;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {

// caminho raiz (\DataSafeEduc\etl_Java\xlsx-extractor> )
// 1. Defina os caminhos dos arquivos
        String caminhoHabilidades = "src\\main\\resources\\matriz_referencia_enem.xlsx";
        String caminhoQuestoes = "src\\main\\resources\\questoesEnem.xlsx";
        String caminhoNotasMunicipio = "src\\main\\resources\\municipioDeSaoPauloResutadosEnem.xlsx";

        // 2. Crie APENAS UM leitor
        LeitorExcelQuestao leitor = new LeitorExcelQuestao();

        // 3. Use o mesmo leitor para carregar as habilidades primeiro
        System.out.println("--- Carregando Habilidades ---");
        leitor.lerHabilidades(caminhoHabilidades);
        leitor.mostrarHabilidades();

        // 4. Use o MESMO leitor para ler as questões
        System.out.println("\n--- Carregando Questões ---");
        leitor.lerQuestoes(caminhoQuestoes);
        leitor.mostrarQuestoes();

        LeitorExcelResultadoDao leitorNotas = new LeitorExcelResultadoDao();

        System.out.println("\n--- Carregando Notas ---");
        leitorNotas.extrairExcelResultado(caminhoNotasMunicipio);
        leitorNotas.calcularMediaTemas();









    }
}