package school.sptech;

public record InatividadeUsuario(
        Integer id,
        String nome,
        String email,
        Integer minutosSemAcesso
) {
}
