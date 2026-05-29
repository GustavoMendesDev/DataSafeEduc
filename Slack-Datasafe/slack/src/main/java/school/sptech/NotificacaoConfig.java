package school.sptech;

import java.util.List;

public class NotificacaoConfig implements ObservadorInatividade {

    private String tokenSlack;
    private String canalSlack;
    private Integer periodicidadeMinutos;
    private Integer limiteDiasSemAcesso;
    private String linkSistema;
    private String dbHost;
    private String dbPort;
    private String dbDatabase;
    private String dbUser;
    private String dbPassword;
    private transient SlackBot slackBot;

    public static NotificacaoConfig carregar() {
        NotificacaoConfig config = new NotificacaoConfig();

        config.tokenSlack = lerVariavel("SLACK_BOT_TOKEN", "xoxb-11164360058514-11148990687367-k6Rt3ej3VblOT71mu2bBoiCj");
        config.canalSlack = lerVariavel("SLACK_CHANNEL_ID", "#equipe-datasafe");
        config.periodicidadeMinutos = lerNumero("PERIODICIDADE_MINUTOS", 180);
        config.limiteDiasSemAcesso = lerNumero("DIAS_SEM_ACESSO", 7);
        config.linkSistema = lerVariavel("LINK_SISTEMA", "http://localhost:8080");
        config.dbHost = lerVariavel("DB_HOST", "localhost");
        config.dbPort = lerVariavel("DB_PORT", "3306");
        config.dbDatabase = lerVariavel("DB_DATABASE", "dataSafe");
        config.dbUser = lerVariavel("DB_USER", "root");
        config.dbPassword = lerVariavel("DB_PASSWORD", "12345678");

        validar(config);

        return config;
    }

    private static String lerVariavel(String nome, String valorPadrao) {
        String valor = System.getenv(nome);

        if (valor == null || valor.isBlank()) {
            return valorPadrao;
        }

        return valor;
    }

    private static Integer lerNumero(String nome, Integer valorPadrao) {
        try {
            return Integer.parseInt(lerVariavel(nome, String.valueOf(valorPadrao)));
        } catch (NumberFormatException erro) {
            return valorPadrao;
        }
    }

    private static void validar(NotificacaoConfig config) {
        if (config.tokenSlack == null || config.tokenSlack.isBlank()) {
            throw new IllegalArgumentException("Configure a variável SLACK_BOT_TOKEN com o token do bot do Slack.");
        }

        if (config.periodicidadeMinutos <= 0) {
            throw new IllegalArgumentException("PERIODICIDADE_MINUTOS precisa ser maior que zero.");
        }

        if (config.limiteDiasSemAcesso < 0) {
            throw new IllegalArgumentException("DIAS_SEM_ACESSO não pode ser negativo.");
        }

        if (config.dbDatabase == null || config.dbDatabase.isBlank()) {
            throw new IllegalArgumentException("Configure a variável DB_DATABASE com o nome do banco.");
        }
    }

    public String montarMensagem(InatividadeUsuario usuario) {
        return """
                :warning: *Alerta de inatividade - Data Safe*

                O usuário *%s* está há *%d dia(s)* sem acessar ou utilizar o sistema.

                Acesse o painel: %s
                """.formatted(usuario.nome(), usuario.diasSemAcesso(), linkSistema);
    }

    public void definirSlackBot(SlackBot slackBot) {
        this.slackBot = slackBot;
    }

    @Override
    public void atualizar(List<InatividadeUsuario> usuariosInativos) {
        if (slackBot == null) {
            throw new IllegalStateException("SlackBot ainda não foi configurado no observador.");
        }

        if (usuariosInativos == null || usuariosInativos.isEmpty()) {
            System.out.println("Nenhum usuário acima do limite de inatividade.");
            return;
        }

        usuariosInativos.forEach(usuario ->
                slackBot.enviarMensagem(montarMensagem(usuario))
        );
    }

    public String getTokenSlack() {
        return tokenSlack;
    }

    public String getCanalSlack() {
        return canalSlack;
    }

    public Integer getPeriodicidadeMinutos() {
        return periodicidadeMinutos;
    }

    public Integer getLimiteDiasSemAcesso() {
        return limiteDiasSemAcesso;
    }

    public String getJdbcUrl() {
        return "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=America/Sao_Paulo"
                .formatted(dbHost, dbPort, dbDatabase);
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
