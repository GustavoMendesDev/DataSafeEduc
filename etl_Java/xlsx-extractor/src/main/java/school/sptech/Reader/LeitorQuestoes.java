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

public class LeitorQuestoes {

    private List<Questao> questoes = new ArrayList<>();
    private List<Habilidade> habilidades;


    public LeitorQuestoes(String nomeArquivoHabilidades) {
        LeitorHabilidades leitorHabilidades = new LeitorHabilidades();
        this.habilidades = leitorHabilidades.lerHabilidades(nomeArquivoHabilidades);

    }

    public void adicionarQuestao(Questao questao) {
        questoes.add(questao);
    }

    public List<Questao> lerQuestoes(String nomeArquivo) {

        try (FileInputStream arquivo = new FileInputStream(nomeArquivo);
             Workbook workbook = new XSSFWorkbook(arquivo);) {
            Integer id = 0;

            Sheet sheetHabilidades = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheetHabilidades.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            info("[] - (LeitorExcelQuestao) - (lerQuestoes) - Leitura do arquivo " + nomeArquivo + " Realizada com sucesso! ");

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

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
//                                System.out.println("Pulando questão duplicada: " + codigoExcel);
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
//                    System.out.println(dificuldadeQuestao);

//                    ConexaoBanco.CONEXAO.update("INSERT INTO parametroTri (id , nivel, parametroA,  parametroB, parametroC ) VALUES (?, ?, ? ,?, ? ) ",
//                            id, dificuldadeQuestao, dificuldade.getParametro_a(), dificuldade.getParametro_b(), dificuldade.getParametro_c());
//
//
//                    ConexaoBanco.CONEXAO.update("INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES (?, ?, ? ,? )",
//                            questao.getCodigoItem(), 2024, questao.getHabildade().getId(), id
//                    );
                    info("[] - (LeitorExcelQuestao) - (lerQuestoes) - Inserção da questão  " + questao.getCodigoItem() + " Realizada com sucesso! ");
                }
            }

        } catch (Exception e) {
            info("Erro " + e);
        }
        info(questoes.size() + " questoes encontradas.");
        return questoes;
    }

    public List<Habilidade> getHabilidades() {
        return habilidades;
    }

}
