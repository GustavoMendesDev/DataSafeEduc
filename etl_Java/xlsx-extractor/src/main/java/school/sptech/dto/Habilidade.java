package school.sptech.dto;

public class Habilidade {
    private String numero;
    private String descricao;
    private AreaConhecimento areaConhecimento;


public Habilidade(){

}
    public Habilidade(String numero, String descricao, AreaConhecimento areaConhecimento, AreaConhecimento areaConhecimento1) {
        this.numero = numero;
        this.descricao = descricao;

        this.areaConhecimento = areaConhecimento1;
    }



    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {

        if(this.numero.length() > 2){
            System.out.println("Habilidade inválida");

            return;
        }

        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public AreaConhecimento getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimento areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }
}
