package school.sptech.Reader;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import school.sptech.S3Service;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import static school.sptech.Log.info;

public class BaseLeitor {

    protected void processarLinha(Row row){}

    public void lerArquivo(String nomeArquivo) {
        try (InputStream arquivo = S3Service.getArquivo(nomeArquivo);
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
