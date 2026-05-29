package school.sptech;

import java.util.ArrayList;
import java.util.List;

public class MonitorInatividade {

    private final InatividadeRepository repository;
    private final List<ObservadorInatividade> observadores;
    private List<InatividadeUsuario> ultimoEstado;

    public MonitorInatividade(InatividadeRepository repository) {
        this.repository = repository;
        this.observadores = new ArrayList<>();
        this.ultimoEstado = List.of();
    }

    public void adicionarObservador(ObservadorInatividade observador) {
        observadores.add(observador);
    }

    public void verificar() {
        List<InatividadeUsuario> estadoAtual = repository.listarUsuariosInativos();

        if (!estadoAtual.equals(ultimoEstado)) {
            System.out.println("Alteração detectada no estado de inatividade. Notificando observadores.");
            ultimoEstado = List.copyOf(estadoAtual);
            notificar(estadoAtual);
            return;
        }

        System.out.println("Sem alteração no estado de inatividade.");
    }

    private void notificar(List<InatividadeUsuario> estadoAtual) {
        for (ObservadorInatividade observador : observadores) {
            observador.atualizar(estadoAtual);
        }
    }
}
