package school.sptech.Reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import school.sptech.enums.SiglaEnum;
import school.sptech.model.Habilidade;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static school.sptech.Log.info;

public class LeitorHabilidades {


    private List<Habilidade> habilidades = new ArrayList<>();


    public void adicionarHabilidade(Habilidade habilidade) {
        habilidades.add(habilidade);
    }

    public List<Habilidade> lerHabilidades(String nomeArquivo) {


        try (FileInputStream arquivo = new FileInputStream(nomeArquivo);
             Workbook workbook = new XSSFWorkbook(arquivo);) {
            info("[] - (LeitorExcelQuestao) - (lerHabilidades) - Leitura do arquivo " + nomeArquivo + " Realizada com sucesso! ");
            Integer id = 0;
            Sheet sheetHabilidades = workbook.getSheetAt(0);


            Iterator<Row> rowIterator = sheetHabilidades.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                Habilidade habilidade = new Habilidade();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch (cell.getColumnIndex()) {
                        case 1:
                            SiglaEnum sigla = SiglaEnum.encontrarSigla(cell.getStringCellValue());

                            habilidade.setSigla(sigla);

                            break;
                        case 2:
                            habilidade.setNumero((int) cell.getNumericCellValue());
                            break;
                        case 3:
                            habilidade.setDescricao(cell.getStringCellValue());
                            break;

                    }


                }


                id++;
                habilidade.setId(id);
                adicionarHabilidade(habilidade);



                info("[] - (LeitorExcelQuestao) - (lerHabilidades) - Inserção da habilidade  " + habilidade.getNumero() + " " + habilidade.getSigla() + " Realizada com sucesso! ");


            }
        } catch (Exception e) {
            info("Erro " + e);
        }

        return habilidades;
    }

    public List<Habilidade> getHabilidades() {
        return habilidades;
    }}
