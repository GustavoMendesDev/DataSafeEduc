package school.sptech;

public record ControleNotificacao(
        Integer id,
        String canalSlack,
        Integer periodoMinutos,
        Boolean notificarSistema,
        Boolean notificarEmail,
        Boolean encerrarSessao
) {
}
