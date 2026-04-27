package school.sptech;

import school.sptech.dao.LeitorExcelQuestaoDao;
import school.sptech.dao.LeitorExcelResultadoDao;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import static school.sptech.TabelasBanco.tabelasBanco;


public class Main {
    public static void main(String[] args)  {

        DefaultCredentialsProvider defaultCredentialsProvider = DefaultCredentialsProvider
                .builder().build();


        // Use it with any service client.
        S3Client s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(defaultCredentialsProvider)
                .build();

        // Now you can use the client with the default credentials chain.
        s3Client.listBuckets();


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