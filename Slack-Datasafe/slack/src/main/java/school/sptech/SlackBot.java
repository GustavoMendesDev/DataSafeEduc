package school.sptech;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

public class SlackBot {

    private final MethodsClient client;
    private final String canal;

    public SlackBot(String token, String canal) {
        this.client = Slack.getInstance().methods(token);
        this.canal = canal;
    }

    public void enviarMensagem(String mensagem) {
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(canal)
                .text(mensagem)
                .build();

        try {
            ChatPostMessageResponse response = client.chatPostMessage(request);

            if (response.isOk()) {
                System.out.println("Mensagem enviada para o Slack com sucesso.");
            } else {
                System.err.println("Erro do Slack: " + response.getError());
            }
        } catch (Exception erro) {
            System.err.println("Erro ao consumir a API do Slack: " + erro.getMessage());
        }
    }
}
