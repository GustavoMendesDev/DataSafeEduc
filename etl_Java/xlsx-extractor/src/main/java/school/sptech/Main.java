package school.sptech;

import school.sptech.dao.MatrizReferenciaExtracaoDao;
import school.sptech.dto.AreaConhecimento;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {


        MatrizReferenciaExtracaoDao dao = new MatrizReferenciaExtracaoDao();

        dao.lerAreaConhecimento("C:\\Users\\gdsmesquita\\Documents\\exercicio\\DataSafeEduc\\etl_Java\\xlsx-extractor\\src\\main\\resources\\matrizReferencia.xlsx");

        dao.mostrarAreasDeConhecimento();
    }
}