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
        String sql = """
                SELECT
                    usuario.id,
                    usuario.nome,
                    COALESCE(
                        DATEDIFF(NOW(), MAX(logAcesso.dataCriacao)),
                        DATEDIFF(NOW(), usuario.dataCriacao),
                        0
                    ) AS diasSemAcesso
                FROM usuario
                LEFT JOIN logAcesso ON logAcesso.fkUsuario = usuario.id
                GROUP BY usuario.id, usuario.nome, usuario.dataCriacao
                HAVING diasSemAcesso >= ?
                ORDER BY diasSemAcesso DESC, usuario.nome ASC;
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
            statement.setInt(1, config.getLimiteDiasSemAcesso());

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    usuarios.add(new InatividadeUsuario(
                            resultado.getInt("id"),
                            resultado.getString("nome"),
                            resultado.getInt("diasSemAcesso")
                    ));
                }
            }
        } catch (Exception erro) {
            System.err.println("Erro ao consultar usuários inativos no banco: " + erro.getMessage());
        }

        return usuarios;
    }
}
