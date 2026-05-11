package school.sptech.Reader;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.ConexaoBanco;

import java.io.FileInputStream;
import java.util.Iterator;

import static school.sptech.Log.info;

public class BaseLeitor {

    protected final JdbcTemplate conexao = ConexaoBanco.CONEXAO;


    protected void processarLinha(Row row){}

    public void lerArquivo(String nomeArquivo) {
        try (FileInputStream arquivo = new FileInputStream(nomeArquivo);
             Workbook workbook = new XSSFWorkbook(arquivo)) {

            info("[] - (BaseLeitor) - Leitura do arquivo " + nomeArquivo + " realizada com sucesso!");

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                processarLinha(row);
            }

        } catch (Exception e) {
            info("[] - (BaseLeitor) - Erro ao ler arquivo " + nomeArquivo + ": " + e);
        }
    }



}
