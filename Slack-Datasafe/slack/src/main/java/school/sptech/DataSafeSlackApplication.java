package school.sptech;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataSafeSlackApplication {

    public static void main(String[] args) {
        NotificacaoConfig config = NotificacaoConfig.carregar();
        SlackBot slackBot = new SlackBot(config.getTokenSlack(), config.getCanalSlack());
        InatividadeRepository inatividadeRepository = new InatividadeRepository(config);
        MonitorInatividade monitorInatividade = new MonitorInatividade(inatividadeRepository);
        config.definirSlackBot(slackBot);
        monitorInatividade.adicionarObservador(config);

        System.out.println("Data Safe Slack iniciado.");
        System.out.println("Canal: " + config.getCanalSlack());
        System.out.println("Periodicidade: 3 hora(s).");
        System.out.println("Limite de inatividade: " + config.getLimiteDiasSemAcesso() + " dia(s).");

        ScheduledExecutorService agendador = Executors.newSingleThreadScheduledExecutor();

        Runnable tarefa = monitorInatividade::verificar;

        agendador.scheduleAtFixedRate(
                tarefa,
                0,
                3,
                TimeUnit.HOURS
        );
    }
}
