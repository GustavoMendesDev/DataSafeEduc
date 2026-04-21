package school.sptech.dto;

public class Dificuldade {
    private final Double parametro_a;
    private final Double parametro_b;
    private final Double parametro_c;


    public Dificuldade(Double parametro_a, Double parametro_b, Double parametro_c) {
        this.parametro_a = parametro_a;
        this.parametro_b = parametro_b;
        this.parametro_c = parametro_c;
    }


    public Double getParametro_a() {
        return parametro_a;
    }

    public Double getParametro_b() {
        return parametro_b;
    }

    public Double getParametro_c() {
        return parametro_c;
    }
}
