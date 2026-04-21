package school.sptech;

import school.sptech.dao.MatrizReferenciaExtracaoDao;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        String nomeArquivo = "matriz_referencia_enem.xlsx";

        MatrizReferenciaExtracaoDao leitor = new MatrizReferenciaExtracaoDao();

        leitor.lerAreaConhecimento(nomeArquivo);

        leitor.mostrarAreasDeConhecimento();
    }
}