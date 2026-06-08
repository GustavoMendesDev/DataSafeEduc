package school.sptech;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

public class SlackBot {

    private final MethodsClient client;
    private boolean ativo;
    private String canal;

    public SlackBot(String token, String canal) {
        this.ativo = token != null && !token.isBlank();
        this.client = ativo ? Slack.getInstance().methods(token) : null;
        this.canal = normalizarCanal(canal);

        if (!ativo) {
            System.out.println("Slack desabilitado: configure SLACK_BOT_TOKEN para enviar mensagens.");
        }
    }

    public void definirCanal(String canal) {
        if (canal != null && !canal.isBlank()) {
            this.canal = normalizarCanal(canal);
        }
    }

    public boolean enviarMensagem(String mensagem) {
        if (!ativo) {
            return false;
        }

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(canal)
                .text(mensagem)
                .build();

        try {
            ChatPostMessageResponse response = client.chatPostMessage(request);

            if (response.isOk()) {
                System.out.println("Mensagem enviada para o Slack com sucesso.");
                return true;
            } else {
                System.err.println("Erro do Slack:");
                System.err.println("error = " + response.getError());
                System.err.println("warning = " + response.getWarning());
                System.err.println("needed = " + response.getNeeded());
                System.err.println("provided = " + response.getProvided());

                desabilitarSeCredencialInvalida(response.getError());
                return false;
            }
        } catch (Exception erro) {
            System.err.println("Erro ao consumir a API do Slack: " + erro.getMessage());
            return false;
        }
    }

    private void desabilitarSeCredencialInvalida(String erroSlack) {
        if ("account_inactive".equals(erroSlack) || "invalid_auth".equals(erroSlack) || "not_authed".equals(erroSlack)) {
            ativo = false;
            System.err.println("Slack desabilitado nesta execução. Configure um SLACK_BOT_TOKEN ativo antes de tentar novamente.");
        }
    }

    private String mensagemErroSlack(String erroSlack) {
        if ("account_inactive".equals(erroSlack)) {
            return "account_inactive. O token configurado pertence a um app/bot ou workspace inativo. Gere um novo Bot User OAuth Token no Slack, instale o app no workspace correto e atualize SLACK_BOT_TOKEN.";
        }

        if ("invalid_auth".equals(erroSlack) || "not_authed".equals(erroSlack)) {
            return erroSlack + ". Configure SLACK_BOT_TOKEN com um Bot User OAuth Token valido, iniciado por xoxb-.";
        }

        return erroSlack;
    }

    private String normalizarCanal(String canal) {
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
}
