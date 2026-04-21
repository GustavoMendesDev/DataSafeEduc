package school.sptech.dao;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import school.sptech.dto.AreaConhecimento;
import school.sptech.dto.Habilidade;
import school.sptech.enums.SiglaEnum;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MatrizReferenciaExtracaoDao {

    List <Habilidade> habilidades = new ArrayList<>();
    List <AreaConhecimento> areasConhecimentos = new ArrayList<>();

    AreaConhecimento areaConhecimento;


    public List <Habilidade> lerHabilidades (String nomeArquivo) {

        File arquivo = new File(nomeArquivo);

        try {
            Workbook workbook = new XSSFWorkbook(arquivo);

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
                        case 2:
                            SiglaEnum sigla = SiglaEnum.encontrarSigla(cell.getStringCellValue());
                            habilidade.setAreaConhecimento(buscarAreaConhecimento(sigla));
                            break;
                        case 3:
                            habilidade.setNumero(cell.getStringCellValue());
                            break;
                        case 4:
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



    public void lerAreaConhecimento (String nomeArquivo) {

        File arquivo = new File(nomeArquivo);

        try {
            Workbook workbook = new XSSFWorkbook(arquivo);

            Sheet sheetHabilidades = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheetHabilidades.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();


                AreaConhecimento areaConhecimento = new AreaConhecimento();
                areasConhecimentos.add(areaConhecimento);
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch (cell.getColumnIndex()) {
                        case 7:
                            areaConhecimento.setNome(cell.getStringCellValue());
                            break;
                        case 8:
                            SiglaEnum sigla = SiglaEnum.encontrarSigla(cell.getStringCellValue());
                            areaConhecimento.setSigla(sigla);
                            break;
                    }


                }
            }
        } catch (Exception e) {
            System.out.println("Erro " + e);
        }

    }

    public AreaConhecimento buscarAreaConhecimento (SiglaEnum siglaEnum){
        for (AreaConhecimento a : areasConhecimentos){
            if(siglaEnum.equals(a.getSigla())){
                return a;
            }
        }
        return null;

    }


    public void mostrarAreasDeConhecimento (){
        for (AreaConhecimento areaConhecimento : areasConhecimentos){
            System.out.println("Nome : " + areaConhecimento.getNome() +
                              "| Sigla : " + areaConhecimento.getSigla());
        }
    }
}
