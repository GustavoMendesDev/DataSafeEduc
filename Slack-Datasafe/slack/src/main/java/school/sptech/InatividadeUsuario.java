package school.sptech;

public record InatividadeUsuario(
        // classe imutável
        Integer id,
        String nome,
        String email,
        Integer minutosSemAcesso
) {
}
