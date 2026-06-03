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
        ControleNotificacao controle = buscarControleNotificacao();
        config.definirControleNotificacao(controle);

        String sql = """
                SELECT
                    usuario.id,
                    usuario.nome,
                    usuario.nome AS email,
                    COALESCE(
                        TIMESTAMPDIFF(MINUTE, MAX(logAcesso.dataCriacao), NOW()),
                        TIMESTAMPDIFF(MINUTE, usuario.dataCriacao, NOW()),
                        0
                    ) AS minutosSemAcesso
                FROM usuario
                LEFT JOIN logAcesso ON logAcesso.fkUsuario = usuario.id
                GROUP BY usuario.id, usuario.nome, usuario.dataCriacao
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
        String sql = """
                SELECT
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
                config.getCanalSlack(),
                config.getPeriodicidadeMinutos(),
                true,
                false,
                false
        );
        config.definirControleNotificacao(controlePadrao);
        return controlePadrao;
    }
}
