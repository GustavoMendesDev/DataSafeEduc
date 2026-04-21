package school.sptech.dto;

import school.sptech.enums.SiglaEnum;

public class AreaConhecimento {
    private String nome;
    private SiglaEnum sigla;

    public AreaConhecimento (){

    }

    public AreaConhecimento(String nome, SiglaEnum sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public SiglaEnum getSigla() {
        return sigla;
    }

    public void setSigla(SiglaEnum sigla) {
        this.sigla = sigla;
    }
}
