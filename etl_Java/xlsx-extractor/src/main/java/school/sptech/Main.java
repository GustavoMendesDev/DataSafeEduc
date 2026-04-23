package school.sptech;

import school.sptech.dao.LeitorExcelQuestao;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {



         String nomeArquivo1 = "src\\main\\resources\\matriz_referencia_enem.xlsx";

         LeitorExcelQuestao leitor1 = new LeitorExcelQuestao();

         leitor1.lerHabilidades(nomeArquivo1);

         leitor1.mostrarhabilidades();



    }
}