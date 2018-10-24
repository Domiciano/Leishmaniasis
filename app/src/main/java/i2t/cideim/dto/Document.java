package i2t.cideim.dto;

import java.util.ArrayList;
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

    public List<UlcerImgDTO> fotoLesiones;
    public PatientDTO patient;

    public static Patient[] findPatientsOfList(Document[] docs) {
        if(docs.length==0) return new Patient[0];
        ArrayList<Patient> patients = new ArrayList<>();
        PatientDTO first = docs[0].patient;
        Patient firstPatient = new Patient(first.id, first.cedula, first.name, first.lastName, first.documentType, first.gender.length()>0?first.gender.charAt(0):'u', first.currentAddress, first.phone, first.birthday, first.province, first.municipality, first.lane, first.contactName, first.contactLastName, first.contactPhone, first.contactCurrentAddress, first.injuryWeeks, docs[0].latitud,docs[0].longitud);
        patients.add(firstPatient);
        if(docs.length==1) return patients.toArray(new Patient[patients.size()]);

        for(int i=1 ; i<docs.length ; i++){
            Patient patient = new Patient(docs[i].patient.id, docs[i].patient.cedula, docs[i].patient.name, docs[i].patient.lastName, docs[i].patient.documentType, docs[i].patient.gender.length()>0?docs[i].patient.gender.charAt(0):'u', docs[i].patient.currentAddress, docs[i].patient.phone, docs[i].patient.birthday, docs[i].patient.province, docs[i].patient.municipality, docs[i].patient.lane, docs[i].patient.contactName, docs[i].patient.contactLastName, docs[i].patient.contactPhone, docs[i].patient.contactCurrentAddress, docs[i].patient.injuryWeeks, docs[0].latitud,docs[0].longitud);
            if(!patients.contains(patient)){
                patients.add(patient);
            }
        }

        return patients.toArray(new Patient[patients.size()]);
    }
}
