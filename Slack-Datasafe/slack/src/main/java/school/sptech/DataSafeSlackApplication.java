package school.sptech;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataSafeSlackApplication {

    public static void main(String[] args) {
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
}
