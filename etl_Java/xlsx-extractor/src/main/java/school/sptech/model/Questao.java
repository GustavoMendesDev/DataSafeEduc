package school.sptech.model;


import school.sptech.enums.SiglaEnum;

import java.util.List;

public class Questao {

    private Integer codigoItem;
    private Habilidade habilidade;
    private Dificuldade dificuldade;
    private String gabarito;
    private SiglaEnum area;



    public Questao(){

    }

    public Questao(Integer codigoItem, Habilidade habilidade, Dificuldade dificuldade,
                   String gabarito) {
        this.codigoItem = codigoItem;
        this.habilidade = habilidade;
        this.dificuldade = dificuldade;
        this.gabarito = gabarito;
    }






    public boolean jaExisteEsseCodigo(List<Questao> listaQuestoes, Integer codQuestao) {

        for (Questao questao : listaQuestoes) {
            if (questao.getCodigoItem() != null && questao.getCodigoItem().equals(codQuestao)) {

                return true;
            }


        }
        return false;
    }









    public Integer getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(Integer codigoItem) {
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
