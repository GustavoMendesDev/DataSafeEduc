package school.sptech;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataSafeSlackApplication {

    public static void main(String[] args) {
        if (args.length > 0 && "--teste-slack".equals(args[0])) {
            enviarMensagemTesteSlack(args);
            return;
        }

        NotificacaoConfig config = NotificacaoConfig.carregar();
        InatividadeRepository inatividadeRepository = new InatividadeRepository(config);
        ControleNotificacao controle = inatividadeRepository.buscarControleNotificacao();
        SlackBot slackBot = new SlackBot(config.getTokenSlack(), config.getCanalSlack());
        EmailService emailService = new EmailService(config);
        MonitorInatividade monitorInatividade = new MonitorInatividade(inatividadeRepository);
        NotificacaoEnvioRepository envioRepository = new NotificacaoEnvioRepository(config);
        CentralNotificacoesService centralNotificacoesService = new CentralNotificacoesService(
                inatividadeRepository,
                envioRepository,
                slackBot,
                config
        );
        config.definirSlackBot(slackBot);
        config.definirEmailService(emailService);
        monitorInatividade.adicionarObservador(config);

        System.out.println("Data Safe Slack iniciado.");
        System.out.println("Canal: " + config.getCanalSlack());
        System.out.println("Periodicidade: " + config.formatarPeriodo(config.getPeriodicidadeMinutos()) + ".");
        System.out.println("Central de notificações: verificando a cada " + config.getIntervaloCentralSegundos() + " segundo(s).");
        System.out.println("E-mail: " + (Boolean.TRUE.equals(controle.notificarEmail()) ? "habilitado" : "desabilitado") + ".");

        centralNotificacoesService.enviarAlertaNotaCorteAoIniciar(controle);

        ScheduledExecutorService agendador = Executors.newSingleThreadScheduledExecutor();

        Runnable tarefaCentral = centralNotificacoesService::verificarConfiguracaoESinalizar;
        Runnable tarefaInatividade = monitorInatividade::verificar;

        agendador.scheduleAtFixedRate(
                tarefaCentral,
                0,
                config.getIntervaloCentralSegundos(),
                TimeUnit.SECONDS
        );

        agendador.scheduleAtFixedRate(
                tarefaInatividade,
                0,
                config.getPeriodicidadeMinutos(),
                TimeUnit.MINUTES
        );
    }

    private static void enviarMensagemTesteSlack(String[] args) {
        NotificacaoConfig config = NotificacaoConfig.carregar();
        String slackToken = config.getTokenSlack();
        String canalId = normalizarCanal(config.getCanalSlack());
        String mensagem = args.length > 1
                ? String.join(" ", Arrays.copyOfRange(args, 1, args.length))
                : "Integração Java com Slack funcionando - DataSafe Educ";

        if (slackToken == null || slackToken.isBlank()) {
            System.err.println("Configure SLACK_BOT_TOKEN antes de enviar mensagem para o Slack.");
            return;
        }

        if (canalId == null || canalId.isBlank()) {
            System.err.println("Configure SLACK_CHANNEL_ID com o ID ou nome do canal do Slack.");
            return;
        }

        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods(slackToken);

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(canalId)
                .text(mensagem)
                .build();

        try {
            ChatPostMessageResponse response = methods.chatPostMessage(request);

            if (response.isOk()) {
                System.out.println("Mensagem enviada com sucesso para o Slack!");
                System.out.println("Canal: " + response.getChannel());
                System.out.println("Timestamp: " + response.getTs());
            } else {
                System.err.println("Erro do Slack: " + mensagemErroSlack(response.getError()));
            }
        } catch (Exception e) {
            System.err.println("Erro ao conectar com a API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String normalizarCanal(String canal) {
        if (canal == null) {
            return null;
        }

        String canalTratado = canal.trim();
        String marcadorUrlSlack = "/client/";

        if (canalTratado.contains(marcadorUrlSlack)) {
            String[] partes = canalTratado.split("/");
            for (int i = 0; i < partes.length; i++) {
                if ("client".equals(partes[i]) && i + 2 < partes.length) {
                    return partes[i + 2];
                }
            }
        }

        return canalTratado;
    }

    private static String mensagemErroSlack(String erroSlack) {
        if ("account_inactive".equals(erroSlack)) {
            return "account_inactive. O token configurado pertence a um app/bot ou workspace inativo. Gere um novo Bot User OAuth Token no Slack, instale o app no workspace correto e atualize SLACK_BOT_TOKEN.";
        }

        if ("invalid_auth".equals(erroSlack) || "not_authed".equals(erroSlack)) {
            return erroSlack + ". Configure SLACK_BOT_TOKEN com um Bot User OAuth Token valido, iniciado por xoxb-.";
        }

        return erroSlack;
    }
}
