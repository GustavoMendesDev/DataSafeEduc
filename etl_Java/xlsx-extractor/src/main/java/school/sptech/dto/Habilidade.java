package school.sptech.dto;

import school.sptech.enums.SiglaEnum;

import java.util.List;

public class Habilidade {
    private Integer id ;
    private Integer numero;
    private String descricao;
    private SiglaEnum sigla;



public Habilidade(){

}
    public Habilidade(Integer numero, String descricao, SiglaEnum sigla) {
        this.numero = numero;
        this.descricao = descricao;
        this.sigla = sigla;


    }



    public static  Habilidade buscarHabilidade (List<Habilidade> habilidades, SiglaEnum sigla, Integer numero ){

        for(Habilidade habilidade : habilidades){
            if(habilidade.getSigla().equals(sigla) &&
                    habilidade.getNumero().equals(numero)){

                return habilidade;

            }
        }

        System.out.println("Habilidade não encontrada!");

        return null;
    }


    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public SiglaEnum getSigla() {
        return sigla;
    }

    public void setSigla(SiglaEnum sigla) {
        this.sigla = sigla;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
