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
        this.canal = canal;

        if (!ativo) {
            System.out.println("Slack desabilitado: configure SLACK_BOT_TOKEN para enviar mensagens.");
        }
    }

    public void definirCanal(String canal) {
        if (canal != null && !canal.isBlank()) {
            this.canal = canal;
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
                System.err.println("Erro do Slack: " + response.getError());
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
            System.err.println("Slack desabilitado nesta execução: token ausente, inválido ou de conta inativa.");
        }
    }
}
