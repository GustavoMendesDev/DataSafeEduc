package school.sptech.dto;

import java.util.ArrayList;
import java.util.List;

public class Nota {

    protected List<Double> notasLc = new ArrayList<>();
    protected List<Double> notasCh = new ArrayList<>();
    protected List<Double> notasMt = new ArrayList<>();
    protected List<Double> notasCn = new ArrayList<>();

    public Nota(List<Double> notasLc, List<Double> notasCh, List<Double> notasMt, List<Double> notasCn) {
        this.notasLc = notasLc;
        this.notasCh = notasCh;
        this.notasMt = notasMt;
        this.notasCn = notasCn;
    }


    public List<Double> getNotasLc() {
        return notasLc;
    }

    public void setNotasLc(List<Double> notasLc) {
        this.notasLc = notasLc;
    }

    public List<Double> getNotasCh() {
        return notasCh;
    }

    public void setNotasCh(List<Double> notasCh) {
        this.notasCh = notasCh;
    }

    public List<Double> getNotasMt() {
        return notasMt;
    }

    public void setNotasMt(List<Double> notasMt) {
        this.notasMt = notasMt;
    }

    public List<Double> getNotasCn() {
        return notasCn;
    }

    public void setNotasCn(List<Double> notasCn) {
        this.notasCn = notasCn;
    }
}
