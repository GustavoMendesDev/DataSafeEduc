package school.sptech.dto;


import school.sptech.enums.SiglaEnum;

public class Questao {

    private String codigoItem;
    private Habilidade habilidade;
    private Dificuldade dificuldade;
    private String gabarito;
    private SiglaEnum area;

    public Questao(){

    }

    public Questao(String codigoItem, Habilidade habilidade, Dificuldade dificuldade,
                   String gabarito) {
        this.codigoItem = codigoItem;
        this.habilidade = habilidade;
        this.dificuldade = dificuldade;
        this.gabarito = gabarito;
    }


    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public Habilidade getHabildade() {
        return habilidade;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public Habilidade getHabilidade() {
        return habilidade;
    }

    public void setHabilidade(Habilidade habilidade) {
        this.habilidade = habilidade;
    }

    public void setDificuldade(Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
    }

    public SiglaEnum getArea() {
        return area;
    }

    public void setArea(SiglaEnum area) {
        this.area = area;
    }

    public String getGabarito() {
        return gabarito;
    }

    public void setGabarito(String gabarito) {
        this.gabarito = gabarito;
    }


    @Override
    public String toString() {
        return "Questao{" +
                "codigoItem='" + codigoItem + '\'' +
                ", habilidade=" + habilidade +
                ", dificuldade=" + dificuldade +
                ", gabarito='" + gabarito + '\'' +
                ", area=" + area +
                '}';
    }
}
