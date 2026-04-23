package school.sptech.dto;

import school.sptech.enums.SiglaEnum;

public class Habilidade {
    private String nome;
    private String descricao;
    private SiglaEnum sigla;



public Habilidade(){

}
    public Habilidade(String numero, String descricao, SiglaEnum sigla) {
        this.nome = numero;
        this.descricao = descricao;
        this.sigla = sigla;


    }



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
}
