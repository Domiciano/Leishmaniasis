package i2t.cideim.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Leonardo and Juan David.
 * Represents an evaluation. Has the required annotations to serialize the data and send it
 * to the server.
 */

@Root(name = "data")
public class Evaluation extends DataXml {

    private static final long serialVersionUID = 1L;
    @Attribute(name = "xsi:type")
    private String classname = "GuaralizadorTestXml";
    @Element(name = "TestId")
    private String UUIDNumber;
    @Element(name = "UlcerasBordesElevados")
    private boolean ulceras;
    @Element(name = "LesionesAgrupadas")
    private boolean agrupadas;
    @Element(name = "LocalizacionCabeza")
    private boolean lesionesH;
    @Element(name = "LocalizacionTronco")
    private boolean lesionesB;
    @Element(name = "LocalizacionBrazoIzquierdo")
    private boolean lesionesLA;
    @Element(name = "LocalizacionBrazoDerecho")
    private boolean lesionesRA;
    @Element(name = "LocalizacionPiernaIzquierda")
    private boolean lesionesLL;
    @Element(name = "LocalizacionPiernaDerecha")
    private boolean lesionesRL;
    @Element(name = "ActividadRiesgo")
    private boolean actividades;
    @Element(name = "Antecedentes")
    private boolean antecedentes;
    @Element(name = "ContactoManta")
    private boolean manta;
    @Element(name = "Date")
    private String date; //dd/MM/yyyy
    @Element(name = "Umbral")
    private double umbral;
    @Element(name = "Puntaje")
    private double score;

    @Element(name = "evaluadorId")
    private String evaluadorId; // Password
    @Element(name = "pacienteId")
    private String pacienteId;

    @Element(name = "ulcerList")
    private List<UlcerImg> ulcerList;

    @Element(name = "hisopoList")
    private List<Hisopo> hisopoList;

    public Evaluation() {
        this.UUIDNumber = UUID.randomUUID().toString();
        this.ulcerList = new ArrayList<UlcerImg>();
        this.hisopoList = new ArrayList<>();
    }

    public Evaluation(boolean ulceras, boolean agrupadas, boolean lesionesH, boolean lesionesB, boolean lesionesLA, boolean lesionesRA, boolean lesionesLL, boolean lesionesRL, boolean actividades, boolean antecedentes, boolean manta, String date) {
        this.UUIDNumber = UUID.randomUUID().toString();
        this.ulceras = ulceras;
        this.agrupadas = agrupadas;
        this.lesionesH = lesionesH;
        this.lesionesB = lesionesB;
        this.lesionesLA = lesionesLA;
        this.lesionesRA = lesionesRA;
        this.lesionesLL = lesionesLL;
        this.lesionesRL = lesionesRL;
        this.actividades = actividades;
        this.antecedentes = antecedentes;
        this.manta = manta;
        this.date = date;
        this.umbral = 0;
        this.score = this.calculateScore();
        this.ulcerList = new ArrayList<>();
        this.hisopoList = new ArrayList<>();
    }

    public Evaluation(String uuid, boolean ulceras, boolean agrupadas, boolean lesionesH, boolean lesionesB, boolean lesionesLA, boolean lesionesRA, boolean lesionesLL, boolean lesionesRL, boolean actividades, boolean antecedentes, boolean manta, String date, int umbral, int score) {
        this.UUIDNumber = uuid;
        this.ulceras = ulceras;
        this.agrupadas = agrupadas;
        this.lesionesH = lesionesH;
        this.lesionesB = lesionesB;
        this.lesionesLA = lesionesLA;
        this.lesionesRA = lesionesRA;
        this.lesionesLL = lesionesLL;
        this.lesionesRL = lesionesRL;
        this.actividades = actividades;
        this.antecedentes = antecedentes;
        this.manta = manta;
        this.date = date;
        this.umbral = umbral;
        this.score = score;
        this.ulcerList = new ArrayList<UlcerImg>();
        this.hisopoList = new ArrayList<>();
    }

    @Override
    public void ParseAttributes() {
    }

    public int calculateScore() {
        return 0;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getUUIDNumber() {
        return UUIDNumber;
    }

    public void setUUIDNumber(String UUIDNumber) {
        this.UUIDNumber = UUIDNumber;
    }

    public boolean isUlceras() {
        return ulceras;
    }

    public void setUlceras(boolean ulceras) {
        this.ulceras = ulceras;
    }

    public boolean isAgrupadas() {
        return agrupadas;
    }

    public void setAgrupadas(boolean agrupadas) {
        this.agrupadas = agrupadas;
    }

    public boolean isLesionesH() {
        return lesionesH;
    }

    public void setLesionesH(boolean lesionesH) {
        this.lesionesH = lesionesH;
    }

    public boolean isLesionesB() {
        return lesionesB;
    }

    public void setLesionesB(boolean lesionesB) {
        this.lesionesB = lesionesB;
    }

    public boolean isLesionesLA() {
        return lesionesLA;
    }

    public void setLesionesLA(boolean lesionesLA) {
        this.lesionesLA = lesionesLA;
    }

    public boolean isLesionesRA() {
        return lesionesRA;
    }

    public void setLesionesRA(boolean lesionesRA) {
        this.lesionesRA = lesionesRA;
    }

    public boolean isLesionesLL() {
        return lesionesLL;
    }

    public void setLesionesLL(boolean lesionesLL) {
        this.lesionesLL = lesionesLL;
    }

    public boolean isLesionesRL() {
        return lesionesRL;
    }

    public void setLesionesRL(boolean lesionesRL) {
        this.lesionesRL = lesionesRL;
    }

    public boolean isActividades() {
        return actividades;
    }

    public void setActividades(boolean actividades) {
        this.actividades = actividades;
    }

    public boolean isAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(boolean antecedentes) {
        this.antecedentes = antecedentes;
    }

    public boolean isManta() {
        return manta;
    }

    public void setManta(boolean manta) {
        this.manta = manta;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getUmbral() {
        return umbral;
    }

    public void setUmbral(double umbral) {
        this.umbral = umbral;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getEvaluadorId() {
        return evaluadorId;
    }

    public void setEvaluadorId(String evaluadorId) {
        this.evaluadorId = evaluadorId;
    }

    public String getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(String pacienteId) {
        this.pacienteId = pacienteId;
    }

    /* Evaluation results */

    private int ulceras_si;
    private int ulceras_no;
    private int agrupadas_si;
    private int agrupadas_no;
    private int lesiones_brazos_si;
    private int lesiones_brazos_no_piernas_no;
    private int lesiones_brazos_no_piernas_si;
    private int actividades_si;
    private int actividades_no;
    private int antecedentes_si;
    private int antecedentes_no;
    private int manta_si;
    private int manta_no;

    public void setEvaluationParameters(int ulceras_si, int ulceras_no, int agrupadas_si, int agrupadas_no, int lesiones_brazos_si, int lesiones_brazos_no_piernas_no, int lesiones_brazos_no_piernas_si, int actividades_si, int actividades_no, int antecedentes_si, int antecedentes_no, int manta_si, int manta_no) {
        this.ulceras_si = ulceras_si;
        this.ulceras_no = ulceras_no;
        this.agrupadas_si = agrupadas_si;
        this.agrupadas_no = agrupadas_no;
        this.lesiones_brazos_si = lesiones_brazos_si;
        this.lesiones_brazos_no_piernas_no = lesiones_brazos_no_piernas_no;
        this.lesiones_brazos_no_piernas_si = lesiones_brazos_no_piernas_si;
        this.actividades_si = actividades_si;
        this.actividades_no = actividades_no;
        this.antecedentes_si = antecedentes_si;
        this.antecedentes_no = antecedentes_no;
        this.manta_si = manta_si;
        this.manta_no = manta_no;
    }

    public static final int EVALUATION_ULCERAS = 1;
    public static final int EVALUATION_LESIONES = 2;
    public static final int EVALUATION_LOCALIZACION = 3;
    public static final int EVALUATION_ACTIVIDADES = 4;
    public static final int EVALUATION_ANTECEDENTES = 5;
    public static final int EVALUATION_MANTA = 6;

    public int getEvaluationResult(int question) {
        switch (question) {
            case EVALUATION_ULCERAS:
                return this.isUlceras() ? ulceras_si : ulceras_no;
            case EVALUATION_LESIONES:
                return this.isAgrupadas() ? agrupadas_si : agrupadas_no;
            case EVALUATION_LOCALIZACION:
                boolean brazos;
                boolean piernas;
                brazos = this.isLesionesLA() || this.isLesionesRA();
                piernas = this.isLesionesLL() || this.isLesionesRL();

                if (brazos) {
                    return lesiones_brazos_si;
                } else if (piernas) {
                    return lesiones_brazos_no_piernas_si;
                } else {
                    return lesiones_brazos_no_piernas_no;
                }
            case EVALUATION_ACTIVIDADES:
                return this.isActividades() ? actividades_si : actividades_no;
            case EVALUATION_ANTECEDENTES:
                return this.isAntecedentes() ? antecedentes_si : antecedentes_no;
            case EVALUATION_MANTA:
                return this.isManta() ? manta_si : manta_no;
        }
        return 0;
    }

    public List<UlcerImg> getUlcerList() {
        return ulcerList;
    }

    public void setUlcerList(List<UlcerImg> ulcerList) {
        this.ulcerList = ulcerList;
    }

    public void addUlcer(UlcerImg img){
        this.ulcerList.add(img);
    }

    public List<Hisopo> getHisopoList() {
        return hisopoList;
    }

    public void setHisopoList(List<Hisopo> hisopoList) {
        this.hisopoList = hisopoList;
    }

    public boolean addAllUlcerImg(Collection<? extends UlcerImg> ulcerImgs){
        return ulcerList.addAll(ulcerImgs);
    }

    public boolean addAllHisopos(Collection<? extends Hisopo> hisopos){
        return hisopoList.addAll(hisopos);
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getUlceras_si() {
        return ulceras_si;
    }

    public void setUlceras_si(int ulceras_si) {
        this.ulceras_si = ulceras_si;
    }

    public int getUlceras_no() {
        return ulceras_no;
    }

    public void setUlceras_no(int ulceras_no) {
        this.ulceras_no = ulceras_no;
    }

    public int getAgrupadas_si() {
        return agrupadas_si;
    }

    public void setAgrupadas_si(int agrupadas_si) {
        this.agrupadas_si = agrupadas_si;
    }

    public int getAgrupadas_no() {
        return agrupadas_no;
    }

    public void setAgrupadas_no(int agrupadas_no) {
        this.agrupadas_no = agrupadas_no;
    }

    public int getLesiones_brazos_si() {
        return lesiones_brazos_si;
    }

    public void setLesiones_brazos_si(int lesiones_brazos_si) {
        this.lesiones_brazos_si = lesiones_brazos_si;
    }

    public int getLesiones_brazos_no_piernas_no() {
        return lesiones_brazos_no_piernas_no;
    }

    public void setLesiones_brazos_no_piernas_no(int lesiones_brazos_no_piernas_no) {
        this.lesiones_brazos_no_piernas_no = lesiones_brazos_no_piernas_no;
    }

    public int getLesiones_brazos_no_piernas_si() {
        return lesiones_brazos_no_piernas_si;
    }

    public void setLesiones_brazos_no_piernas_si(int lesiones_brazos_no_piernas_si) {
        this.lesiones_brazos_no_piernas_si = lesiones_brazos_no_piernas_si;
    }

    public int getActividades_si() {
        return actividades_si;
    }

    public void setActividades_si(int actividades_si) {
        this.actividades_si = actividades_si;
    }

    public int getActividades_no() {
        return actividades_no;
    }

    public void setActividades_no(int actividades_no) {
        this.actividades_no = actividades_no;
    }

    public int getAntecedentes_si() {
        return antecedentes_si;
    }

    public void setAntecedentes_si(int antecedentes_si) {
        this.antecedentes_si = antecedentes_si;
    }

    public int getAntecedentes_no() {
        return antecedentes_no;
    }

    public void setAntecedentes_no(int antecedentes_no) {
        this.antecedentes_no = antecedentes_no;
    }

    public int getManta_si() {
        return manta_si;
    }

    public void setManta_si(int manta_si) {
        this.manta_si = manta_si;
    }

    public int getManta_no() {
        return manta_no;
    }

    public void setManta_no(int manta_no) {
        this.manta_no = manta_no;
    }
}
