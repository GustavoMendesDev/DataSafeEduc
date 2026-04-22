package school.sptech.dao;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import school.sptech.dto.Habilidade;
import school.sptech.enums.SiglaEnum;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MatrizReferenciaExtracaoDao {

    List <Habilidade> habilidades = new ArrayList<>();



    public List <Habilidade> lerHabilidades (String nomeArquivo) {



        try (FileInputStream arquivo = new FileInputStream(nomeArquivo);
             Workbook workbook = new XSSFWorkbook(arquivo);){
            System.out.println("[] - (MatrizReferenciaExtracaoDao) - Leitura do arquivo " + nomeArquivo + " Realizada com sucesso! ");

            Sheet sheetHabilidades = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheetHabilidades.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                Habilidade habilidade = new Habilidade();

                habilidades.add(habilidade);

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch (cell.getColumnIndex()) {
                        case 1:
                            SiglaEnum sigla = SiglaEnum.encontrarSigla(cell.getStringCellValue());

                            habilidade.setSigla(sigla);
                            break;
                        case 2:
                            habilidade.setNome(cell.getStringCellValue());
                            break;
                        case 3:
                            habilidade.setDescricao(cell.getStringCellValue());
                            break;

                    }


                }
            }
        } catch (Exception e) {
            System.out.println("Erro " + e);
        }

        return habilidades;
    }





    public void mostrarhabilidades (){
        for (Habilidade habilidade : habilidades){
            System.out.println(
             "Sigla : "+ habilidade.getSigla() +
            "| Habilidade : "+ habilidade.getNome() +
            "| Descricao: "+ habilidade.getDescricao() );
        }
    }
}
