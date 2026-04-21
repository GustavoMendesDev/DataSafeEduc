package school.sptech.dto;


public class Questao {

    private String codigoItem;
    private Habilidade habilidade;
    private Dificuldade dificuldade;
    private String gabarito;

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



    public String getGabarito() {
        return gabarito;
    }

    public void setGabarito(String gabarito) {
        this.gabarito = gabarito;
    }
}
