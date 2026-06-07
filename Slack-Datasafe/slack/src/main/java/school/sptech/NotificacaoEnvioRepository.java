package school.sptech;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NotificacaoEnvioRepository {

    private final NotificacaoConfig config;

    public NotificacaoEnvioRepository(NotificacaoConfig config) {
        this.config = config;
    }

    public void garantirTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS controleNotificacaoEnvio (
                    id INT NOT NULL AUTO_INCREMENT,
                    fkControleNotificacao INT NOT NULL,
                    tipo VARCHAR(80) NOT NULL,
                    dataEnvio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_controle_tipo (fkControleNotificacao, tipo)
                );
                """;

        try (
                Connection conexao = DriverManager.getConnection(
                        config.getJdbcUrl(),
                        config.getDbUser(),
                        config.getDbPassword()
                );
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.executeUpdate();
        } catch (Exception erro) {
            System.err.println("Erro ao garantir tabela controleNotificacaoEnvio: " + erro.getMessage());
        }
    }

    public boolean jaFoiEnviado(Integer controleId, String tipo) {
        if (controleId == null || tipo == null || tipo.isBlank()) {
            return false;
        }

        garantirTabela();

        String sql = """
                SELECT 1
                FROM controleNotificacaoEnvio
                WHERE fkControleNotificacao = ?
                  AND tipo = ?
                LIMIT 1;
                """;

        try (
                Connection conexao = DriverManager.getConnection(
                        config.getJdbcUrl(),
                        config.getDbUser(),
                        config.getDbPassword()
                );
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setInt(1, controleId);
            statement.setString(2, tipo);

            try (ResultSet resultado = statement.executeQuery()) {
                return resultado.next();
            }
        } catch (Exception erro) {
            System.err.println("Erro ao consultar histórico de envio: " + erro.getMessage());
            return false;
        }
    }

    public void registrarEnvio(Integer controleId, String tipo) {
        if (controleId == null || tipo == null || tipo.isBlank()) {
            return;
        }

        garantirTabela();

        String sql = """
                INSERT IGNORE INTO controleNotificacaoEnvio (fkControleNotificacao, tipo)
                VALUES (?, ?);
                """;

        try (
                Connection conexao = DriverManager.getConnection(
                        config.getJdbcUrl(),
                        config.getDbUser(),
                        config.getDbPassword()
                );
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setInt(1, controleId);
            statement.setString(2, tipo);
            statement.executeUpdate();
        } catch (Exception erro) {
            System.err.println("Erro ao registrar envio de notificação: " + erro.getMessage());
        }
    }
}
