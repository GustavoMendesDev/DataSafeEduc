package school.sptech.Reader;

<<<<<<< HEAD
=======
import org.apache.poi.ss.usermodel.Cell;
>>>>>>> dashboard
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import school.sptech.S3Service;
<<<<<<< HEAD


=======
import com.github.pjfanning.xlsx.StreamingReader;
>>>>>>> dashboard
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import static school.sptech.Log.info;

public class BaseLeitor {

    protected void processarLinha(Row row){}

    public void lerArquivo(String nomeArquivo) {
<<<<<<< HEAD
        try (InputStream arquivo = S3Service.getArquivo(nomeArquivo);
             Workbook workbook = new XSSFWorkbook(arquivo)) {
=======
        long tempoInicial = System.currentTimeMillis();


        // try (InputStream arquivo = S3Service.getArquivo(nomeArquivo);
       try(// InputStream arquivo = S3Service.getArquivo(nomeArquivo);
               InputStream arquivo = getClass().getClassLoader().getResourceAsStream(nomeArquivo);
         Workbook workbook = StreamingReader.builder().rowCacheSize(10).bufferSize(256).open(arquivo)
       //Workbook workbook =  new XSSFWorkbook(arquivo)
       ) {
>>>>>>> dashboard

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
<<<<<<< HEAD
    }


=======
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
>>>>>>> dashboard

}
