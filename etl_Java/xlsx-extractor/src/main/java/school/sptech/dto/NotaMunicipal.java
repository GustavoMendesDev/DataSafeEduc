package school.sptech.dto;

import javax.swing.text.StyledEditorKit;

public class NotaMunicipal {

    private Double mediaCienciasNatureza;
    private Double mediaCienciasHumanas;
    private Double mediaMatematica;
    private Double mediaCodigosLinguagens;


    public NotaMunicipal(Double mediaCienciasNatureza, Double mediaCienciasHumanas, Double mediaMatematica, Double mediaCodigosLinguagens) {
        this.mediaCienciasNatureza = mediaCienciasNatureza;
        this.mediaCienciasHumanas = mediaCienciasHumanas;
        this.mediaMatematica = mediaMatematica;
        this.mediaCodigosLinguagens = mediaCodigosLinguagens;
    }




    public Double getMediaCienciasNatureza() {
        return mediaCienciasNatureza;
    }

    public void setMediaCienciasNatureza(Double mediaCienciasNatureza) {
        this.mediaCienciasNatureza = mediaCienciasNatureza;
    }

    public Double getMediaCienciasHumanas() {
        return mediaCienciasHumanas;
    }

    public void setMediaCienciasHumanas(Double mediaCienciasHumanas) {
        this.mediaCienciasHumanas = mediaCienciasHumanas;
    }

    public Double getMediaMatematica() {
        return mediaMatematica;
    }

    public void setMediaMatematica(Double mediaMatematica) {
        this.mediaMatematica = mediaMatematica;
    }

    public Double getMediaCodigosLinguagens() {
        return mediaCodigosLinguagens;
    }

    public void setMediaCodigosLinguagens(Double mediaCodigosLinguagens) {
        this.mediaCodigosLinguagens = mediaCodigosLinguagens;
    }
}
