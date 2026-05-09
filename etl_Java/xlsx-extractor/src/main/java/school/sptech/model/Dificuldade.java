package school.sptech.model;

public class Dificuldade {
    private Integer id;
    private String nivel;
    private  Double parametro_a;
    private  Double parametro_b;
    private  Double parametro_c;



    public Dificuldade(Double parametro_a, Double parametro_b, Double parametro_c) {
        this.parametro_a = parametro_a;
        this.parametro_b = parametro_b;
        this.parametro_c = parametro_c;

    }

    public Dificuldade(){

    }


    public String calcularDificuldade(double parametro_b) {
        if (parametro_b < -0.5) {
            this.nivel = "Facil";
        } else if (parametro_b <= 0.8) {
            this.nivel = "Medio";
        } else {
            this.nivel = "Dificil";
        }
        return this.nivel; // ← salva e retorna
    }

    public Double getParametro_a() {
        return parametro_a;
    }

    public void setParametro_a(Double parametro_a) {
        this.parametro_a = parametro_a;
    }

    public Double getParametro_b() {
        return parametro_b;
    }

    public void setParametro_b(Double parametro_b) {
        this.parametro_b = parametro_b;
    }

    public Double getParametro_c() {
        return parametro_c;
    }

    public void setParametro_c(Double parametro_c) {
        this.parametro_c = parametro_c;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
