package school.sptech;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class CentralNotificacoesService {

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String TIPO_SISU_NOTAS_CORTE = "SISU_NOTAS_CORTE";

    private final InatividadeRepository inatividadeRepository;
    private final NotificacaoEnvioRepository envioRepository;
    private final SlackBot slackBot;
    private final NotificacaoConfig config;

    public CentralNotificacoesService(
            InatividadeRepository inatividadeRepository,
            NotificacaoEnvioRepository envioRepository,
            SlackBot slackBot,
            NotificacaoConfig config
    ) {
        this.inatividadeRepository = inatividadeRepository;
        this.envioRepository = envioRepository;
        this.slackBot = slackBot;
        this.config = config;
    }

    public void verificarConfiguracaoESinalizar() {
        inatividadeRepository.listarControlesNotificacaoAtivos().forEach(this::processarControle);
    }

    public void enviarAlertaNotaCorteAoIniciar(ControleNotificacao controle) {
        if (controle != null) {
            slackBot.definirCanal(controle.canalSlack());
        }

        boolean enviado = slackBot.enviarMensagem(mensagemSisuNotasCorte());

        if (enviado && controle != null && controle.id() != null) {
            envioRepository.registrarEnvio(controle.id(), TIPO_SISU_NOTAS_CORTE);
        }
    }

    private void processarControle(ControleNotificacao controle) {
        if (controle == null || controle.id() == null) {
            System.out.println("Nenhuma configuração de notificação cadastrada no banco.");
            return;
        }

        slackBot.definirCanal(controle.canalSlack());

        Map<String, String> mensagens = montarMensagens(controle);

        mensagens.forEach((tipo, mensagem) -> enviarSePendente(controle.id(), tipo, mensagem));
    }

    private Map<String, String> montarMensagens(ControleNotificacao controle) {
        Map<String, String> mensagens = new LinkedHashMap<>();

        if (Boolean.TRUE.equals(controle.notificarSistema())) {
            mensagens.put("ATUALIZACAO_ENEM", mensagemAtualizacaoEnem());
            mensagens.put(TIPO_SISU_NOTAS_CORTE, mensagemSisuNotasCorte());
        }

        if (Boolean.TRUE.equals(controle.notificarEmail())) {
            mensagens.put("REGIAO_CONCORRENCIA", mensagemRegiaoConcorrencia());
        }

        if (Boolean.TRUE.equals(controle.encerrarSessao())) {
            mensagens.put("TENDENCIAS_EDUCACIONAIS", mensagemTendenciasEducacionais());
        }

        mensagens.put("RELATORIOS_GRAFICOS", mensagemRelatoriosGraficos());
        mensagens.put("SEGURANCA_ACESSOS", mensagemSegurancaAcessos());

        return mensagens;
    }

    private void enviarSePendente(Integer controleId, String tipo, String mensagem) {
        if (envioRepository.jaFoiEnviado(controleId, tipo)) {
            System.out.println("Notificação já enviada para controle " + controleId + ": " + tipo);
            return;
        }

        boolean enviado = slackBot.enviarMensagem(mensagem);

        if (enviado) {
            envioRepository.registrarEnvio(controleId, tipo);
        }
    }

    private String cabecalho(String titulo) {
        return ":bell: *" + titulo + " - DataSafe Educ*\n"
                + "Configuração processada em " + LocalDateTime.now().format(FORMATADOR_DATA) + ".\n\n";
    }

    private String mensagemAtualizacaoEnem() {
        return cabecalho("Atualizações do ENEM")
                + "A equipe solicitou receber atualizações do ENEM pelo Slack.\n"
                + "Acompanhe publicações oficiais, cronograma, provas, gabaritos e comunicados relevantes para atualizar as análises do painel.\n\n"
                + "Painel: " + config.getLinkSistema();
    }

    private String mensagemSisuNotasCorte() {
        return cabecalho("Alertas de SISU e notas de corte")
                + "Acompanhe as atualizações de notas de corte e mudanças do SISU.\n"
                + "Revise regras, pesos, cronograma e possíveis impactos nas aprovações acompanhadas pelo cursinho.\n"
                + "Use esta notificação como lembrete padrão para validar os indicadores no DataSafe Educ.\n\n"
                + "Painel: " + config.getLinkSistema();
    }

    private String mensagemRegiaoConcorrencia() {
        return cabecalho("Insights por região e concorrência")
                + "O cliente ativou insights de desempenho por região e concorrência.\n"
                + "Use as notas municipais e os indicadores por área para comparar desempenho regional e priorizar conteúdos de maior impacto.\n\n"
                + "Painel: " + config.getLinkSistema();
    }

    private String mensagemTendenciasEducacionais() {
        return cabecalho("Tendências educacionais")
                + "O cliente ativou insights de tendências educacionais.\n"
                + "Monitore habilidades recorrentes, áreas com maior dificuldade e práticas pedagógicas que possam orientar novas ações de ensino.\n\n"
                + "Painel: " + config.getLinkSistema();
    }

    private String mensagemRelatoriosGraficos() {
        return cabecalho("Relatórios e gráficos")
                + "Relatórios e gráficos devem ser revisados após a consolidação dos dados no DataSafe Educ.\n"
                + "Confira os dashboards de desempenho por tema, habilidade e questão.\n\n"
                + "Painel: " + config.getLinkSistema();
    }

    private String mensagemSegurancaAcessos() {
        return cabecalho("Segurança e acessos")
                + "A Central de Notificações está acompanhando o log de acessos do sistema.\n"
                + "Verifique logins recentes, possíveis acessos simultâneos e comportamentos incomuns no painel administrativo.\n\n"
                + "Painel: " + config.getLinkSistema();
    }
}
