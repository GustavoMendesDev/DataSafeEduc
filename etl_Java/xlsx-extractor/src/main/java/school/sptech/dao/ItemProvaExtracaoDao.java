package school.sptech.dao;
import school.sptech.dto.Questao;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ItemProvaExtracaoDao {

    List<Questao> questoes = new ArrayList<>();

    public List <Questao> lerQuestoes (String nomeArquivo) {



    File arquivo = new File(nomeArquivo);

   try {


       Workbook workbook = new XSSFWorkbook(arquivo);

       Sheet sheetQuestoes = workbook.getSheetAt(0);

       Iterator<Row> rowIterator = sheetQuestoes.iterator();

       if(rowIterator.hasNext()) {
            rowIterator.next();
       }

       while (rowIterator.hasNext()) {
           Row row = rowIterator.next();
           Iterator<Cell> cellIterator = row.cellIterator();

           Questao questao = new Questao();
           questoes.add(questao);

           while (cellIterator.hasNext()) {
               Cell cell = cellIterator.next();

               //Switch
           }
       }
   }catch (Exception e){
       e.printStackTrace();
   }


   return questoes;
    }
}




