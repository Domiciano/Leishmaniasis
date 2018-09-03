package i2t.cideim.dto;

import java.util.List;

import i2t.cideim.model.Patient;
import i2t.cideim.model.UlcerImg;

public class Document {
    public String id;
    public String lastDateUpdate;
    public String pacienteId;
    public String evaluadorId;
    public String date;

    public int umbral;
    public int puntaje;
    public int injuryWeeks;

    public boolean lesionesAgrupadas;
    public boolean ulcerasBordesElevados;
    public boolean localizacionCabeza;
    public boolean localizacionTronco;
    public boolean localizacionBrazoIzquierdo;
    public boolean localizacionBrazoDerecho;
    public boolean localizacionPiernaIzquierda;
    public boolean localizacionPiernaDerecha;
    public boolean actividadRiesgo;
    public boolean antecedentes;
    public boolean contactoManta;

    public int cantidadFoto;
    public double latitud;
    public double longitud;

    public int numeroTotalLesiones;
    public int numeroHisopos;

    public List<UlcerImg> fotoLesiones;
    public Patient patient;
}
