package school.sptech.Reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import school.sptech.S3Service;
//import com.github.pjfanning.xlsx.StreamingReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import static school.sptech.Log.info;

public class BaseLeitor {

    protected void processarLinha(Row row){}

    public void lerArquivo(String nomeArquivo) {
        long tempoInicial = System.currentTimeMillis();


        try (InputStream arquivo = S3Service.getArquivo(nomeArquivo);
       //try(InputStream arquivo = S3Service.getArquivo(nomeArquivo);   como instalar o maven linux ubuntu
               //InputStream arquivo = getClass().getClassLoader().getResourceAsStream(nomeArquivo);
        // Workbook workbook = StreamingReader.builder().rowCacheSize(10).bufferSize(256).open(arquivo)
       Workbook workbook =  new XSSFWorkbook(arquivo)
       ) {

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
        long tempoFinal = System.currentTimeMillis();
        System.out.printf("%.3f s%n", (tempoFinal - tempoInicial) / 1000d);
    }


    protected Double extrairValorNumerico(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                String valor = cell.getStringCellValue().trim();
                if (valor.isEmpty()) return null;
                try {
                    return Double.parseDouble(valor);
                } catch (NumberFormatException e) {
                    return null;
                }
            case BLANK:
                return null;
            default:
                return null;
        }
    }

}
