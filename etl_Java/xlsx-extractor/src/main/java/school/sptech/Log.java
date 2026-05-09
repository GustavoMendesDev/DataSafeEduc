
package school.sptech;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import school.sptech.ConexaoBanco;

import school.sptech.util.Data;


public class Log {

    // Atributo constante para formatar data e hora.
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



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

        System.out.println(
                "[" + Data.mostrarDataAtual() + "] "
                        + "[" + nivel + "] - "
                        + mensagem
        );

        // Tratamento de exceções + inserção no banco
        try {

            String ip =
                    InetAddress.getLocalHost()
                            .getHostAddress();

            ConexaoBanco.CONEXAO.update(
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
                    "[" + Data.mostrarDataAtual() + "] "
                            + "[ERRO] - Falha ao salvar log no banco"
            );
        }
    }
}

