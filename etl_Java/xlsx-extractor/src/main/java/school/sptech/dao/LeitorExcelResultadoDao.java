package school.sptech.dao;
import school.sptech.dto.NotaMunicipal;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LeitorExcelResultadoDao {

    private List<Double> notasLc = new ArrayList<>();
    private List<Double> notasCh = new ArrayList<>();
    private List<Double> notasMt = new ArrayList<>();
    private List<Double> notasCn = new ArrayList<>();


    public void adicionarNotaLc(Double nota){
        notasLc.add(nota);
    }

    public void adicionarNotaCn(Double nota){
        notasCn.add(nota);
    }

    public void adicionarNotaCh(Double nota){
        notasCh.add(nota);
    }

    public void adicionarNotaMt(Double nota){
        notasMt.add(nota);
    }


    public void extrairExcelResultado (String nomeArquivo) {


        try (FileInputStream arquivo = new FileInputStream(nomeArquivo);
             Workbook workbook = new XSSFWorkbook(arquivo);){
            System.out.println("[] - (leitorExcelResultadoDao) - Leitura do arquivo " + nomeArquivo + " Realizada com sucesso! ");

       Sheet sheetQuestoes = workbook.getSheetAt(0);

       Iterator<Row> rowIterator = sheetQuestoes.iterator();

       if(rowIterator.hasNext()) {
            rowIterator.next();

       }


       while (rowIterator.hasNext()) {
           Row row = rowIterator.next();
           Iterator<Cell> cellIterator = row.cellIterator();


           while (cellIterator.hasNext()) {
               Cell cell = cellIterator.next();

               switch (cell.getColumnIndex()) {
                   case 22:
                       Double notaCn = extrairValorNumerico(cell);
                       if (notaCn != null) adicionarNotaCn(notaCn);
                       break;
                   case 23:
                       Double notaCh = extrairValorNumerico(cell);
                       if (notaCh != null) adicionarNotaCh(notaCh);
                       break;
                   case 24:
                       Double notaLc = extrairValorNumerico(cell);
                       if (notaLc != null) adicionarNotaLc(notaLc);
                       break;
                   case 25:
                       Double notaMt = extrairValorNumerico(cell);
                       if (notaMt != null) adicionarNotaMt(notaMt);
                       break;
               }
           }
       }

            System.out.println("Leitura da notas realizadas com sucesso!");
   }catch (Exception e){
       e.printStackTrace();
   }
  }


  public NotaMunicipal calcularMediaTemas() {



      Double mediaLc = notasLc.isEmpty() ? 0.0 :
              notasLc.stream().mapToDouble(Double::doubleValue).sum() / notasLc.size();

      Double mediaMt = notasMt.isEmpty() ? 0.0 :
              notasMt.stream().mapToDouble(Double::doubleValue).sum() / notasMt.size();

      Double mediaCn = notasCn.isEmpty() ? 0.0 :
              notasCn.stream().mapToDouble(Double::doubleValue).sum() / notasCn.size();

      Double mediaCh = notasCh.isEmpty() ? 0.0 :
              notasCh.stream().mapToDouble(Double::doubleValue).sum() / notasCh.size();

          NotaMunicipal notaMunicipal = new NotaMunicipal(mediaCn, mediaCh, mediaMt, mediaLc);


      System.out.print(mediaCn + " " +mediaCh + " " +  mediaMt + " " + mediaLc);
      System.out.println(notasCn.size() + " " +notasCh.size() + " " +  notasMt.size() + " " + notasLc.size());

            return  notaMunicipal;
  }


    private Double extrairValorNumerico(Cell cell) {
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




