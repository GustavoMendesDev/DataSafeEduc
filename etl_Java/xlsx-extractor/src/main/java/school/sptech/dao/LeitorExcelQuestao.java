package school.sptech.dao;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import school.sptech.dto.Dificuldade;
import school.sptech.dto.Habilidade;
import school.sptech.dto.Questao;
import school.sptech.enums.SiglaEnum;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LeitorExcelQuestao {

    List <Habilidade> habilidades = new ArrayList<>();

    List<Questao> questoes = new ArrayList<>();



    public List <Habilidade> lerHabilidades (String nomeArquivo) {

        try (FileInputStream arquivo = new FileInputStream(nomeArquivo);
             Workbook workbook = new XSSFWorkbook(arquivo);){
            System.out.println("[] - (LeitorExcelQuestao) - Leitura do arquivo " + nomeArquivo + " Realizada com sucesso! ");

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



    public List <Questao> lerQuestoes (String nomeArquivo) {

        try (FileInputStream arquivo = new FileInputStream(nomeArquivo);
             Workbook workbook = new XSSFWorkbook(arquivo);){

            System.out.println("[] - (LeitorExcelQuestao) - (lerQuestoes) - Leitura do arquivo " + nomeArquivo + " Realizada com sucesso! ");

            Sheet sheetHabilidades = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheetHabilidades.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                Questao questao = new Questao();

                questoes.add(questao);

                Dificuldade dificuldade = new Dificuldade();

                SiglaEnum sigla;

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch (cell.getColumnIndex()) {
                        case 2:
                            Boolean codigoExistente = jaExisteEsseCodigo(cell.getStringCellValue());

                            if (!codigoExistente) {
                                questao.setCodigoItem(cell.getStringCellValue());
                                break;
                            }
                            questoes.remove(questao);

                            cellIterator.hasNext();


                        case 1:
                            sigla = SiglaEnum.encontrarSigla(cell.getStringCellValue());

                            questao.setArea(sigla);
                            break;
                        case 4:
                            String nomeHabilidade = "H" + cell.getStringCellValue();
                            Habilidade habilidade = buscarHabilidade(questao.getArea(), nomeHabilidade);
                            questao.setHabilidade(habilidade);

                            break;
                        case 3:
                            questao.setGabarito(cell.getStringCellValue());
                            break;
                        case 7:
                            dificuldade.setParametro_a(cell.getNumericCellValue());
                            break;
                        case 8:
                            dificuldade.setParametro_b(cell.getNumericCellValue());
                            break;
                        case 9:
                            dificuldade.setParametro_c(cell.getNumericCellValue());
                            questao.setDificuldade(dificuldade);
                            break;
                    }


                }
            }
        } catch (Exception e) {
            System.out.println("Erro " + e);
        }

        return questoes;
    }



    public void mostrarhabilidades (){
        for (Habilidade habilidade : habilidades){
            System.out.println(
             "Sigla : "+ habilidade.getSigla() +
            "| Habilidade : "+ habilidade.getNome() +
            "| Descricao: "+ habilidade.getDescricao() );
        }
    }

    public Habilidade buscarHabilidade (SiglaEnum sigla, String nome ){

        for(Habilidade habilidade : habilidades){
            if(habilidade.getSigla().equals(sigla) &&
               habilidade.getNome().equals(nome)
            ){
                return habilidade;

            }
        }

        System.out.println("Habilidade não encontrada!");

        return null;
    }

    public boolean jaExisteEsseCodigo (String codQuestao) {

        for (Questao questao : questoes){
            if(questao.getCodigoItem().equals(codQuestao)){
                return true;
            }
        }
        return false;
    }
}
