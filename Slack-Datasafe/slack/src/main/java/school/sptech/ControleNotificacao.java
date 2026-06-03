package school.sptech;

public record ControleNotificacao(
        String canalSlack,
        Integer periodoMinutos,
        Boolean notificarSistema,
        Boolean notificarEmail,
        Boolean encerrarSessao
) {
}
