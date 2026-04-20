package school.sptech.dto;


public class Questao {

    private String codigoItem;
    private String habilidade;
    private String respostaGabarito;
    private String areaConhecimento;
    private Double descriminacao;
    private Double dificuldade;
    private Double probabilidadeAcertos;

    public Questao() {

    }

    public Questao(String codigoItem, String habilidade, String respostaGabarito,
                       String areaConhecimento, Double descriminacao, Double dificuldade,
                       Double probabilidadeAcertos) {
        this.codigoItem = codigoItem;
        this.habilidade = habilidade;
        this.respostaGabarito = respostaGabarito;
        this.areaConhecimento = areaConhecimento;
        this.descriminacao = descriminacao;
        this.dificuldade = dificuldade;
        this.probabilidadeAcertos = probabilidadeAcertos;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public String getHabilidade() {
        return habilidade;
    }

    public void setHabilidade(String habilidade) {
        this.habilidade = habilidade;
    }

    public String getRespostaGabarito() {
        return respostaGabarito;
    }

    public void setRespostaGabarito(String respostaGabarito) {
        this.respostaGabarito = respostaGabarito;
    }

    public String getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(String areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    public Double getDescriminacao() {
        return descriminacao;
    }

    public void setDescriminacao(Double descriminacao) {
        this.descriminacao = descriminacao;
    }

    public Double getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(Double dificuldade) {
        this.dificuldade = dificuldade;
    }

    public Double getProbabilidadeAcertos() {
        return probabilidadeAcertos;
    }

    public void setProbabilidadeAcertos(Double probabilidadeAcertos) {
        this.probabilidadeAcertos = probabilidadeAcertos;
    }
}
