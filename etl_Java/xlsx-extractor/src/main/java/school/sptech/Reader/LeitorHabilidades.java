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

public class LeitorHabilidades  extends  BaseLeitor{


    private List<Habilidade> habilidades = new ArrayList<>();
    private Integer id = 0;


    public void adicionarHabilidade(Habilidade habilidade) {
        habilidades.add(habilidade);
    }

    @Override
    protected void processarLinha(Row row) {
        Habilidade habilidade = new Habilidade();
        Iterator<Cell> cellIterator = row.cellIterator();

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

        info("[] - (LeitorHabilidades) - (processarLinha) - Habilidade " + habilidade.getNumero()
                + " [" + habilidade.getSigla() + "] carregada.");
    }

    public List<Habilidade> getHabilidades() {
        return habilidades;
    }
}


