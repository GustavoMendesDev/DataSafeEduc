package school.sptech.dao;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.ConexaoBanco;
import school.sptech.dto.Dificuldade;
import school.sptech.dto.Habilidade;
import school.sptech.dto.Questao;
import school.sptech.enums.SiglaEnum;
import school.sptech.TabelasBanco;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static school.sptech.Log.*;

import static school.sptech.dto.Habilidade.buscarHabilidade;

public class LeitorExcelQuestaoDao {

    private List<Habilidade> habilidades = new ArrayList<>();
    private List<Questao> questoes = new ArrayList<>();


    ConexaoBanco conexaoBanco = new ConexaoBanco();
    JdbcTemplate conexao = conexaoBanco.getConnection();


    public void adicionarHabilidade(Habilidade habilidade) {
        habilidades.add(habilidade);
    }

    public void adicionarQuestao(Questao questao) {
        questoes.add(questao);
    }


    public void removerHabilidade(Habilidade habilidade) {
        for (int i = 0; i < habilidades.size(); i++) {
            if (habilidades.get(i).equals(habilidade)) {
                habilidades.remove(i);
            }
        }
    }

    public void removerQuestao(Questao questao) {
        for (int i = 0; i < questoes.size(); i++) {
            if (questoes.get(i).equals(questao)) {
                questoes.remove(i);
                i--;
            }
        }
    }


    public List<Habilidade> lerHabilidades(String nomeArquivo) {

        conexao.update("INSERT INTO areaConhecimento (id, nome , sigla) VALUES (?, ?, ?) ",
                1, "Linguagens e Códigos", "LC");

        conexao.update("INSERT INTO areaConhecimento (id, nome , sigla) VALUES (?, ?, ?) ",
                2, "Matematica", "MT");
        conexao.update("INSERT INTO areaConhecimento (id, nome , sigla) VALUES (?, ?, ?) ",
                3, "Ciências da Natureza", "CN");

        conexao.update("INSERT INTO areaConhecimento (id, nome , sigla) VALUES (?, ?, ?) ",
                4, "Ciências Humanas", "CH");

        try (FileInputStream arquivo = new FileInputStream(nomeArquivo);
             Workbook workbook = new XSSFWorkbook(arquivo);) {
            info("[] - (LeitorExcelQuestao) - (lerHabilidades) - Leitura do arquivo " + nomeArquivo + " Realizada com sucesso! ");
            Integer id = 0;
            Sheet sheetHabilidades = workbook.getSheetAt(0);
            Integer idAreaConhecimento = 0;

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

                            idAreaConhecimento = sigla.getCodigo();

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

                conexao.update("INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (?, ?, ?, ? ) ",
                        id, habilidade.getNumero(), habilidade.getDescricao(), idAreaConhecimento);

                info("[] - (LeitorExcelQuestao) - (lerHabilidades) - Inserção da habilidade  " + habilidade.getNumero() + " " + habilidade.getSigla() + " Realizada com sucesso! ");


            }
        } catch (Exception e) {
            info("Erro " + e);
        }

        return habilidades;
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

                    conexao.update("INSERT INTO parametroTri (id , nivel, parametroA,  parametroB, parametroC ) VALUES (?, ?, ? ,?, ? ) ",
                            id, dificuldadeQuestao, dificuldade.getParametro_a(), dificuldade.getParametro_b(), dificuldade.getParametro_c());


                    conexao.update("INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES (?, ?, ? ,? )",
                            questao.getCodigoItem(), 2024, questao.getHabildade().getId(), id
                    );
                    info("[] - (LeitorExcelQuestao) - (lerQuestoes) - Inserção da questão  " + questao.getCodigoItem() + " Realizada com sucesso! ");
                }
            }

        } catch (Exception e) {
            info("Erro " + e);
        }
        info(questoes.size() + " questoes encontradas.");
        return questoes;
    }


    public void mostrarHabilidades() {
        for (Habilidade habilidade : habilidades) {
            info(
                    "Sigla : " + habilidade.getSigla() +
                            "| Habilidade : " + habilidade.getNumero() +
                            "| Descricao: " + habilidade.getDescricao());
        }
    }

    public void mostrarQuestoes() {
        for (Questao questao : questoes) {
            System.out.println(questao.toString());
        }
    }


}
