package school.sptech.dao;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.ConexaoBanco;
import school.sptech.dto.NotaMunicipal;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LeitorExcelResultadoDao {

    private final List<Double> notasLc = new ArrayList<>();
    private final List<Double> notasCh = new ArrayList<>();
    private final List<Double> notasMt = new ArrayList<>();
    private final List<Double> notasCn = new ArrayList<>();

    private final JdbcTemplate conexao = new ConexaoBanco().getConnection();

    public void extrairExcelResultado(InputStream stream) {
        notasLc.clear();
        notasCh.clear();
        notasMt.clear();
        notasCn.clear();

        try (Workbook workbook = new XSSFWorkbook(stream)) {
            System.out.println("[] - (LeitorExcelResultadoDao) - Leitura OK");

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // header

            while (rows.hasNext()) {
                Row row = rows.next();

                // colunas 22..25
                addIfNotNull(notasCn, extrairValorNumerico(row.getCell(22)));
                addIfNotNull(notasCh, extrairValorNumerico(row.getCell(23)));
                addIfNotNull(notasLc, extrairValorNumerico(row.getCell(24)));
                addIfNotNull(notasMt, extrairValorNumerico(row.getCell(25)));
            }

            System.out.println("Leitura das notas realizada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addIfNotNull(List<Double> lista, Double v) {
        if (v != null) lista.add(v);
    }

    public NotaMunicipal calcularMediaTemas() {
        Double mediaLc = media(notasLc);
        Double mediaMt = media(notasMt);
        Double mediaCn = media(notasCn);
        Double mediaCh = media(notasCh);

        NotaMunicipal notaMunicipal = new NotaMunicipal(mediaCn, mediaCh, mediaMt, mediaLc);

        conexao.update(
                "INSERT INTO notaMunicipal (matematica, codigosELinguagens, cienciasDaNatureza, cienciasHumanas) VALUES (?, ?, ?, ?)",
                mediaMt, mediaLc, mediaCn, mediaCh
        );

        System.out.printf("[] - Inseriu notas: CN=%.2f CH=%.2f MT=%.2f LC=%.2f%n", mediaCn, mediaCh, mediaMt, mediaLc);
        return notaMunicipal;
    }

    private Double media(List<Double> lista) {
        return lista.isEmpty() ? 0.0 : lista.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private Double extrairValorNumerico(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                String valor = cell.getStringCellValue().trim();
                if (valor.isEmpty()) return null;
                try {
                    return Double.parseDouble(valor.replace(",", "."));
                } catch (NumberFormatException e) {
                    return null;
                }
            case BLANK:
            default:
                return null;
        }
    }
}