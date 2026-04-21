package school.sptech.enums;



public enum SiglaEnum {




    LC("Linguagens e Códigos" ),
    MT("Matemática"),
    CN("Ciências da Natureza" ),
    CH("Ciências Humanas");



    private String areaConhecimento;

    SiglaEnum(String areaConhecimento  ) {
        this.areaConhecimento = areaConhecimento;

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
}
