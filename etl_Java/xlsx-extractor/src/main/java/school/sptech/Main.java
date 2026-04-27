package school.sptech;

import school.sptech.dao.LeitorExcelQuestaoDao;
import school.sptech.dao.LeitorExcelResultadoDao;

import java.io.InputStream;

import static school.sptech.TabelasBanco.tabelasBanco;

public class Main {
    public static void main(String[] args) {

        tabelasBanco();

        String bucket = "data-safe-s3";
        S3Downloader s3 = new S3Downloader(bucket);

        LeitorExcelQuestaoDao leitor = new LeitorExcelQuestaoDao();

        System.out.println("--- Carregando Habilidades ---");
        try (InputStream in = s3.baixarArquivo("matriz_referencia_enem.xlsx")) {
            leitor.lerHabilidades(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n--- Carregando Questões ---");
        try (InputStream in = s3.baixarArquivo("questoesEnem.xlsx")) {
            leitor.lerQuestoes(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LeitorExcelResultadoDao leitorNotas = new LeitorExcelResultadoDao();

        System.out.println("\n--- Carregando Notas ---");
        try (InputStream in = s3.baixarArquivo("municipioDeSaoPauloResutadosEnem.xlsx")) {
            leitorNotas.extrairExcelResultado(in);
            leitorNotas.calcularMediaTemas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}