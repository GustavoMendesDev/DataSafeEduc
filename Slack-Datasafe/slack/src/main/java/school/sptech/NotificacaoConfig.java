package school.sptech;

import java.util.List;

public class NotificacaoConfig implements ObservadorInatividade {

    private String tokenSlack;
    private String canalSlack;
    private Integer periodicidadeMinutos;
    private Integer intervaloCentralSegundos;
    private Integer limiteDiasSemAcesso;
    private String linkSistema;
    private String dbHost;
    private String dbPort;
    private String dbDatabase;
    private String dbUser;
    private String dbPassword;
    private String smtpHost;
    private String smtpPort;
    private String smtpUser;
    private String smtpPassword;
    private String smtpFrom;
    private Boolean smtpStartTls;
    private ControleNotificacao controleNotificacao;
    private transient SlackBot slackBot;
    private transient EmailService emailService;

    public static NotificacaoConfig carregar() {
        NotificacaoConfig config = new NotificacaoConfig();

        config.tokenSlack = lerVariavel("SLACK_BOT_TOKEN", "");
        config.canalSlack = lerVariavel("SLACK_CHANNEL_ID", "#equipe-datasafe");
        config.periodicidadeMinutos = lerNumero("PERIODICIDADE_MINUTOS", 180);
        config.intervaloCentralSegundos = lerNumero("INTERVALO_CENTRAL_SEGUNDOS", 30);
        config.limiteDiasSemAcesso = lerNumero("DIAS_SEM_ACESSO", 7);
        config.linkSistema = lerVariavel("LINK_SISTEMA", "http://localhost:8080");
        config.dbHost = lerVariavel("DB_HOST", "localhost");
        config.dbPort = lerVariavel("DB_PORT", "3306");
        config.dbDatabase = lerVariavel("DB_DATABASE", "dataSafe");
        config.dbUser = lerVariavel("DB_USER", "root");
        config.dbPassword = lerVariavel("DB_PASSWORD", "12345678");
        config.smtpHost = lerVariavel("SMTP_HOST", "");
        config.smtpPort = lerVariavel("SMTP_PORT", "587");
        config.smtpUser = lerVariavel("SMTP_USER", "");
        config.smtpPassword = lerVariavel("SMTP_PASSWORD", "");
        config.smtpFrom = lerVariavel("SMTP_FROM", config.smtpUser);
        config.smtpStartTls = Boolean.parseBoolean(lerVariavel("SMTP_STARTTLS", "true"));
        config.controleNotificacao = new ControleNotificacao(
                null,
                config.canalSlack,
                config.periodicidadeMinutos,
                true,
                false,
                false
        );

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
        if (config.periodicidadeMinutos <= 0) {
            throw new IllegalArgumentException("PERIODICIDADE_MINUTOS precisa ser maior que zero.");
        }

        if (config.intervaloCentralSegundos <= 0) {
            throw new IllegalArgumentException("INTERVALO_CENTRAL_SEGUNDOS precisa ser maior que zero.");
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

                O usuário *%s* está há *%s* sem acessar ou utilizar o sistema.

                Acesse o painel: %s
                """.formatted(usuario.nome(), formatarPeriodo(usuario.minutosSemAcesso()), linkSistema);
    }

    public void definirSlackBot(SlackBot slackBot) {
        this.slackBot = slackBot;
    }

    public void definirEmailService(EmailService emailService) {
        this.emailService = emailService;
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

        ControleNotificacao controle = configAtualBanco();
        slackBot.definirCanal(controle.canalSlack());

        usuariosInativos.forEach(usuario -> {
            slackBot.enviarMensagem(montarMensagem(usuario));

            if (Boolean.TRUE.equals(controle.notificarEmail()) && emailService != null) {
                emailService.enviarEmail(usuario, formatarPeriodo(usuario.minutosSemAcesso()));
            }
        });
    }

    public ControleNotificacao configAtualBanco() {
        return controleNotificacao;
    }

    public void definirControleNotificacao(ControleNotificacao controleNotificacao) {
        if (controleNotificacao == null) {
            return;
        }

        this.controleNotificacao = controleNotificacao;
        setCanalSlack(controleNotificacao.canalSlack());
        setPeriodicidadeMinutos(controleNotificacao.periodoMinutos());
    }

    public String formatarPeriodo(Integer minutos) {
        if (minutos == null) {
            return "0 minuto";
        }

        if (minutos < 60) {
            return minutos + " minuto(s)";
        }

        if (minutos < 1440) {
            return (minutos / 60) + " hora(s)";
        }

        return (minutos / 1440) + " dia(s)";
    }

    public String getTokenSlack() {
        return tokenSlack;
    }

    public Boolean isSlackConfigurado() {
        return tokenSlack != null && !tokenSlack.isBlank();
    }

    public String getCanalSlack() {
        return canalSlack;
    }

    public void setCanalSlack(String canalSlack) {
        if (canalSlack != null && !canalSlack.isBlank()) {
            this.canalSlack = canalSlack;
        }
    }

    public Integer getPeriodicidadeMinutos() {
        return periodicidadeMinutos;
    }

    public Integer getIntervaloCentralSegundos() {
        return intervaloCentralSegundos;
    }

    public void setPeriodicidadeMinutos(Integer periodicidadeMinutos) {
        if (periodicidadeMinutos != null && periodicidadeMinutos > 0) {
            this.periodicidadeMinutos = periodicidadeMinutos;
        }
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

    public String getLinkSistema() {
        return linkSistema;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public String getSmtpUser() {
        return smtpUser;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public String getSmtpFrom() {
        return smtpFrom == null || smtpFrom.isBlank() ? smtpUser : smtpFrom;
    }

    public Boolean isSmtpStartTls() {
        return smtpStartTls;
    }

    public Boolean isEmailConfigurado() {
        return smtpHost != null && !smtpHost.isBlank()
                && smtpUser != null && !smtpUser.isBlank()
                && smtpPassword != null && !smtpPassword.isBlank();
    }
}
