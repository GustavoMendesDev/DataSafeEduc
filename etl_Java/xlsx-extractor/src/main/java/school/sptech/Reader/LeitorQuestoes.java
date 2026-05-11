package school.sptech.Reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import school.sptech.enums.SiglaEnum;
import school.sptech.model.Dificuldade;
import school.sptech.model.Habilidade;
import school.sptech.model.Questao;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static school.sptech.Log.info;
import static school.sptech.model.Habilidade.buscarHabilidade;
import school.sptech.Reader.LeitorHabilidades;

public class LeitorQuestoes extends BaseLeitor {

    private List<Questao> questoes = new ArrayList<>();
    private List<Habilidade> habilidades;


    public LeitorQuestoes(String nomeArquivoHabilidades) {
        LeitorHabilidades leitorHabilidades = new LeitorHabilidades();
        this.habilidades = leitorHabilidades.getHabilidades();

    }

    public void adicionarQuestao(Questao questao) {
        questoes.add(questao);
    }

    @Override
    public void processarLinha(Row row) {
        Integer id = 0;

        Boolean questaoDuplicada = false;
        String dificuldadeQuestao = "";
        Iterator<Cell> cellIterator = row.cellIterator();

        Questao questao = new Questao();

        Dificuldade dificuldade = new Dificuldade();

        SiglaEnum sigla;

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 1:
                    sigla = SiglaEnum.encontrarSigla(cell.getStringCellValue());
                    questao.setArea(sigla);
                    break;

                case 2:
                    int codigoExcel = (int) cell.getNumericCellValue();

                    if (questao.jaExisteEsseCodigo(questoes, codigoExcel)) {
                        questaoDuplicada = true;
                    } else {
                        questao.setCodigoItem(codigoExcel);
                    }
                    break;

                case 3:
                    questao.setGabarito(cell.getStringCellValue());

                    break;

                case 4:
                    Integer numero = (int) cell.getNumericCellValue();

                    Habilidade habilidade = buscarHabilidade(habilidades, questao.getArea(), numero);
                    questao.setHabilidade(habilidade);

                    break;
                case 7:

                    dificuldade.setParametro_a(cell.getNumericCellValue());
                    break;
                case 8:
                    dificuldadeQuestao = dificuldade.calcularDificuldade(cell.getNumericCellValue());
                    dificuldade.setParametro_b(cell.getNumericCellValue());
                    break;
                case 9:

                    dificuldade.setParametro_c(cell.getNumericCellValue());
                    questao.setDificuldade(dificuldade);
                    break;
            }


        }


        if (!questaoDuplicada) {
            id++;
            dificuldade.setId(id);

            adicionarQuestao(questao);

            info("[] - (LeitorExcelQuestao) - (lerQuestoes) - Inserção da questão  " + questao.getCodigoItem() + " Realizada com sucesso! ");
        }
        info(questoes.size() + " questoes encontradas.");

    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public List<Habilidade> getHabilidades() {
        return habilidades;
    }



}


