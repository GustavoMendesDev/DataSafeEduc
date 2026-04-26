package school.sptech.enums;


import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.ConexaoBanco;


public enum SiglaEnum {


    LC("Linguagens e Códigos", 1),
    MT("Matemática", 2),
    CN("Ciências da Natureza", 3),
    CH("Ciências Humanas", 4);


    private String areaConhecimento;
    private Integer codigo;


    ConexaoBanco conexaoBanco = new ConexaoBanco();
    JdbcTemplate conexao = conexaoBanco.getConnection();

    SiglaEnum(String areaConhecimento, Integer codigo) {
        this.areaConhecimento = areaConhecimento;
        this.codigo = codigo;

    }



    public static SiglaEnum encontrarSigla(String valorCelula) {

        return switch (valorCelula) {
            case "MT" -> SiglaEnum.MT;
            case "CN" -> SiglaEnum.CN;
            case "CH" -> SiglaEnum.CH;
            default -> SiglaEnum.LC;
        };



    }




    public String getAreaConhecimento() {
        return areaConhecimento;
    }

    public Integer getCodigo() {
        return codigo;
    }
}
