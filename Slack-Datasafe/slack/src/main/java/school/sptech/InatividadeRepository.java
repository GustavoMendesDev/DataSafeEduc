package school.sptech;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InatividadeRepository {

    private final NotificacaoConfig config;

    public InatividadeRepository(NotificacaoConfig config) {
        this.config = config;
    }

    public List<InatividadeUsuario> listarUsuariosInativos() {
        garantirTabelaControleNotificacao();
        ControleNotificacao controle = buscarControleNotificacao();
        config.definirControleNotificacao(controle);

        String sql = """
                SELECT
                    usuario.id,
                    usuario.nome,
                    usuario.email,
                    COALESCE(
                        TIMESTAMPDIFF(MINUTE, MAX(logAcesso.dataCriacao), NOW()),
                        TIMESTAMPDIFF(MINUTE, usuario.dataCriacao, NOW()),
                        0
                    ) AS minutosSemAcesso
                FROM usuario
                LEFT JOIN logAcesso ON logAcesso.fkUsuario = usuario.id
                GROUP BY usuario.id, usuario.nome, usuario.email, usuario.dataCriacao
                HAVING minutosSemAcesso >= ?
                ORDER BY minutosSemAcesso DESC, usuario.nome ASC;
                """;

        List<InatividadeUsuario> usuarios = new ArrayList<>();

        try (
                Connection conexao = DriverManager.getConnection(
                        config.getJdbcUrl(),
                        config.getDbUser(),
                        config.getDbPassword()
                );
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setInt(1, controle.periodoMinutos());

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    usuarios.add(new InatividadeUsuario(
                            resultado.getInt("id"),
                            resultado.getString("nome"),
                            resultado.getString("email"),
                            resultado.getInt("minutosSemAcesso")
                    ));
                }
            }
        } catch (Exception erro) {
            System.err.println("Erro ao consultar usuários inativos no banco: " + erro.getMessage());
        }

        return usuarios;
    }

    public ControleNotificacao buscarControleNotificacao() {
        garantirTabelaControleNotificacao();

        String sql = """
                SELECT
                    id,
                    slack_channel_id,
                    periodo,
                    notificarSistema,
                    notificarEmail,
                    encerrarSessao
                FROM controleNotificacao
                WHERE ativo = 1
                ORDER BY id DESC
                LIMIT 1;
                """;

        try (
                Connection conexao = DriverManager.getConnection(
                        config.getJdbcUrl(),
                        config.getDbUser(),
                        config.getDbPassword()
                );
                PreparedStatement statement = conexao.prepareStatement(sql);
                ResultSet resultado = statement.executeQuery()
        ) {
            if (resultado.next()) {
                ControleNotificacao controle = new ControleNotificacao(
                        resultado.getInt("id"),
                        resultado.getString("slack_channel_id"),
                        resultado.getInt("periodo"),
                        resultado.getBoolean("notificarSistema"),
                        resultado.getBoolean("notificarEmail"),
                        resultado.getBoolean("encerrarSessao")
                );
                config.definirControleNotificacao(controle);
                return controle;
            }
        } catch (Exception erro) {
            System.err.println("Erro ao consultar controleNotificacao no banco: " + erro.getMessage());
        }

        ControleNotificacao controlePadrao = new ControleNotificacao(
                null,
                config.getCanalSlack(),
                config.getPeriodicidadeMinutos(),
                true,
                false,
                false
        );
        config.definirControleNotificacao(controlePadrao);
        return controlePadrao;
    }

    public List<ControleNotificacao> listarControlesNotificacaoAtivos() {
        garantirTabelaControleNotificacao();

        String sql = """
                SELECT
                    controleNotificacao.id,
                    controleNotificacao.slack_channel_id,
                    controleNotificacao.periodo,
                    controleNotificacao.notificarSistema,
                    controleNotificacao.notificarEmail,
                    controleNotificacao.encerrarSessao
                FROM controleNotificacao
                JOIN (
                    SELECT MAX(id) AS id
                    FROM controleNotificacao
                    WHERE ativo = 1
                    GROUP BY COALESCE(usuario_id, id)
                ) ultimosControles ON ultimosControles.id = controleNotificacao.id
                ORDER BY controleNotificacao.id ASC;
                """;

        List<ControleNotificacao> controles = new ArrayList<>();

        try (
                Connection conexao = DriverManager.getConnection(
                        config.getJdbcUrl(),
                        config.getDbUser(),
                        config.getDbPassword()
                );
                PreparedStatement statement = conexao.prepareStatement(sql);
                ResultSet resultado = statement.executeQuery()
        ) {
            while (resultado.next()) {
                controles.add(new ControleNotificacao(
                        resultado.getInt("id"),
                        resultado.getString("slack_channel_id"),
                        resultado.getInt("periodo"),
                        resultado.getBoolean("notificarSistema"),
                        resultado.getBoolean("notificarEmail"),
                        resultado.getBoolean("encerrarSessao")
                ));
            }
        } catch (Exception erro) {
            System.err.println("Erro ao listar controles de notificação ativos: " + erro.getMessage());
        }

        if (controles.isEmpty()) {
            controles.add(buscarControleNotificacao());
        }

        return controles;
    }

    public void garantirTabelaControleNotificacao() {
        String sql = """
                CREATE TABLE IF NOT EXISTS controleNotificacao (
                    id INT NOT NULL AUTO_INCREMENT,
                    slack_channel_id VARCHAR(100) NOT NULL,
                    periodo INT NOT NULL,
                    receberNotificacao VARCHAR(3) NOT NULL,
                    tipoNotificacao TINYINT(1) NOT NULL,
                    notificarSistema TINYINT(1) NOT NULL,
                    notificarEmail TINYINT(1) NOT NULL,
                    encerrarSessao TINYINT(1) NOT NULL,
                    ativo TINYINT(1) NOT NULL DEFAULT 1,
                    usuario_id INT NULL,
                    PRIMARY KEY (id)
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
            System.err.println("Erro ao garantir tabela controleNotificacao: " + erro.getMessage());
        }
    }
}
