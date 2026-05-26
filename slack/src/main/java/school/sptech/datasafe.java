package school.sptech;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

public class datasafe {

    public static void main(String[] args) {
        String slackToken = "xoxb-11164360058514-11148990687367-k6Rt3ej3VblOT71mu2bBoiCj";

        String canalId = "#equipe-datasafe";

        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods(slackToken);

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(canalId)
                .text("Integração Java com Slack funcionando")
                .build();

        try {
            ChatPostMessageResponse response = methods.chatPostMessage(request);

            if (response.isOk()) {
                System.out.println("Mensagem enviada com sucesso para o Slack!");
            } else {
                System.err.println("Erro do Slack: " + response.getError());
            }
        } catch (Exception e) {
            System.err.println("Erro ao conectar com a API: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
