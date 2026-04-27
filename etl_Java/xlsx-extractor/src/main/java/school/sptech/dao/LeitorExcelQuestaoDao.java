package school.sptech.dao;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.ConexaoBanco;
import school.sptech.dto.Dificuldade;
import school.sptech.dto.Habilidade;
import school.sptech.dto.Questao;
import school.sptech.enums.SiglaEnum;

import java.io.InputStream;
import java.util.*;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;
import static school.sptech.dto.Habilidade.buscarHabilidade;

public class LeitorExcelQuestaoDao {

    private final List<Habilidade> habilidades = new ArrayList<>();
    private final List<Questao> questoes = new ArrayList<>();

    private final JdbcTemplate conexao = new ConexaoBanco().getConnection();

    public List<Habilidade> lerHabilidades(InputStream stream) {
        habilidades.clear();

        try (Workbook workbook = new XSSFWorkbook(stream)) {
            System.out.println("[] - (LeitorExcelQuestaoDao) - (lerHabilidades) - Leitura OK");

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // header

            int nextId = obterProximoId("habilidade");

            while (rows.hasNext()) {
                Row row = rows.next();

                // Colunas esperadas: 1=sigla, 2=numero, 3=descricao
                String siglaStr = getString(row, 1);
                Integer numero = getInt(row, 2);
                String descricao = getString(row, 3);

                if (siglaStr == null || numero == null || descricao == null || descricao.isBlank()) {
                    // pula linhas inválidas
                    continue;
                }

                SiglaEnum sigla = SiglaEnum.encontrarSigla(siglaStr.trim());
                if (sigla == null) continue;

                Habilidade hab = new Habilidade();
                hab.setId(nextId);
                hab.setSigla(sigla);
                hab.setNumero(numero);
                hab.setDescricao(descricao);

                habilidades.add(hab);

                // fkAreaConhecimento = sigla.getCodigo() (assumindo isso)
                conexao.update(
                        "INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (?, ?, ?, ?)",
                        hab.getId(), hab.getNumero(), hab.getDescricao(), sigla.getCodigo()
                );

                System.out.println("[] - Inseriu habilidade: " + hab.getSigla() + " " + hab.getNumero());
                nextId++;
            }

        } catch (Exception e) {
            System.out.println("Erro (lerHabilidades): " + e.getMessage());
            e.printStackTrace();
        }

        return habilidades;
    }

    public List<Questao> lerQuestoes(InputStream stream) {
        questoes.clear();

        try (Workbook workbook = new XSSFWorkbook(stream)) {
            System.out.println("[] - (LeitorExcelQuestaoDao) - (lerQuestoes) - Leitura OK");

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // header

            int nextTriId = obterProximoId("parametroTri");

            while (rows.hasNext()) {
                Row row = rows.next();

                // Colunas: 1=sigla, 2=codigoItem, 3=gabarito, 4=numeroHabilidade, 7=a, 8=b, 9=c
                String siglaStr = getString(row, 1);
                Integer codigoItem = getInt(row, 2);

                // se faltou dado essencial, pula
                if (siglaStr == null || codigoItem == null) continue;

                SiglaEnum sigla = SiglaEnum.encontrarSigla(siglaStr.trim());
                if (sigla == null) continue;

                // duplicidade: se já existe no "questoes" ou no banco, pule
                if (jaExisteCodigoNaLista(codigoItem) || jaExisteCodigoNoBanco(codigoItem)) {
                    continue;
                }

                Questao questao = new Questao();
                questao.setArea(sigla);
                questao.setCodigoItem(codigoItem);

                String gabarito = getString(row, 3);
                if (gabarito != null) questao.setGabarito(gabarito);

                Integer numeroHab = getInt(row, 4);
                if (numeroHab == null) continue;

                Habilidade habilidade = buscarHabilidade(habilidades, sigla, numeroHab);
                if (habilidade == null) {
                    // habilidade não encontrada — evita NPE e pula
                    continue;
                }
                questao.setHabilidade(habilidade);

                Double a = getDouble(row, 7);
                Double b = getDouble(row, 8);
                Double c = getDouble(row, 9);

                if (a == null || b == null || c == null) {
                    // sem TRI completo, pula
                    continue;
                }

                Dificuldade dificuldade = new Dificuldade();
                dificuldade.setId(nextTriId);
                dificuldade.setParametro_a(a);
                dificuldade.setParametro_b(b);
                dificuldade.setParametro_c(c);

                // IMPORTANTE: calcular depois de ter os parâmetros
                String nivel = dificuldade.calcularDificuldade(b);
                questao.setDificuldade(dificuldade);

                // persiste
                conexao.update(
                        "INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (?, ?, ?, ?, ?)",
                        nextTriId, nivel, a, b, c
                );

                conexao.update(
                        "INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES (?, ?, ?, ?)",
                        questao.getCodigoItem(), 2024, habilidade.getId(), nextTriId
                );

                questoes.add(questao);
                System.out.println("[] - Inseriu questão: " + questao.getCodigoItem());

                nextTriId++;
            }

        } catch (Exception e) {
            System.out.println("Erro (lerQuestoes): " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println(questoes.size() + " questões inseridas.");
        return questoes;
    }

    // ---------------- helpers ----------------

    private String getString(Row row, int col) {
        Cell cell = row.getCell(col, RETURN_BLANK_AS_NULL);
        if (cell == null) return null;

        DataFormatter fmt = new DataFormatter();
        String v = fmt.formatCellValue(cell);
        return (v == null || v.isBlank()) ? null : v.trim();
    }

    private Integer getInt(Row row, int col) {
        Cell cell = row.getCell(col, RETURN_BLANK_AS_NULL);
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            }
            String s = new DataFormatter().formatCellValue(cell).trim();
            if (s.isBlank()) return null;
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }

    private Double getDouble(Row row, int col) {
        Cell cell = row.getCell(col, RETURN_BLANK_AS_NULL);
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
            String s = new DataFormatter().formatCellValue(cell).trim();
            if (s.isBlank()) return null;
            return Double.parseDouble(s.replace(",", "."));
        } catch (Exception e) {
            return null;
        }
    }

    private boolean jaExisteCodigoNaLista(int codigo) {
        return questoes.stream().anyMatch(q -> q.getCodigoItem() == codigo);
    }

    private boolean jaExisteCodigoNoBanco(int codigo) {
        Integer count = conexao.queryForObject(
                "SELECT COUNT(*) FROM questao WHERE codigoItem = ?",
                Integer.class,
                codigo
        );
        return count != null && count > 0;
    }

    private int obterProximoId(String tabela) {
        Integer max = conexao.queryForObject("SELECT COALESCE(MAX(id), 0) FROM " + tabela, Integer.class);
        return (max == null ? 1 : max + 1);
    }
}