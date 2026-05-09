
package school.sptech;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.jdbc.core.JdbcTemplate;

public class Log {

    // Atributo constante para formatar data e hora.
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Atributo constante para conexao com o banco de dados.
    private static final ConexaoBanco CONEXAO_BANCO =
            new ConexaoBanco();

    // atributo para simplifação do código referente também à conexão do bd
    private static final JdbcTemplate CONEXAO =
            CONEXAO_BANCO.getConnection();

    // Métodos para informar o tipo de log que será salvo no banco
    // INFO ex: "banco conectado"
    // ERRO ex: "Falha ao ler excel"
    // WARN ex: "Questão duplicada"
    public static void info(String mensagem) {

        salvarLog("INFO", mensagem);
    }

    public static void erro(String mensagem) {

        salvarLog("ERRO", mensagem);
    }

    public static void warning(String mensagem) {

        salvarLog("WARNING", mensagem);
    }

    // Método para formatar as mensagens dos logs no terminal
    private static void salvarLog(
            String nivel,
            String mensagem
    ) {

        String dataHora =
                LocalDateTime.now()
                        .format(FORMATTER);

        System.out.println(
                "[" + dataHora + "] "
                        + "[" + nivel + "] - "
                        + mensagem
        );

        // Tratamento de exceções + inserção no banco
        try {

            String ip =
                    InetAddress.getLocalHost()
                            .getHostAddress();

            CONEXAO.update(
                    """
                            INSERT INTO logAcesso
                            (mensagem, nivel, ip, dataCriacao)
                            VALUES (?, ?, ?, ?)
                            """,
                    mensagem,
                    nivel,
                    ip,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            System.out.println(
                    "[" + dataHora + "] "
                            + "[ERRO] - Falha ao salvar log no banco"
            );
        }
    }
}

