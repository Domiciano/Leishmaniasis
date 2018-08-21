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
 * Represents an patient. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "data")
public class Patient extends DataXml {

    private static final long serialVersionUID = 1L;
    @Attribute(name = "xsi:type")
    private String classname = "PacienteXml";
    @Element(name = "UserId")
    private String UUIDNumber;
    @Element(name = "Cedula")
    private String identification;
    @Element(name = "Name")
    private String name;
    @Element(name = "LastName")
    private String lastName;
    @Element(name = "DocumentType")
    private String documentType;
    @Element(name = "Gender")
    private char genre;
    @Element(name = "CurrentAddress")
    private String address;
    @Element(name = "Phone")
    private String phone;
    @Element(name = "Birthday")
    private String birthday; //dd/MM/yyyy
    @Element(name = "Province")
    private String province;
    @Element(name = "Municipality")
    private String municipality;
    @Element(name = "Lane")
    private String lane;
    @Element(name = "ContactName")
    private String contactName;
    @Element(name = "ContactLastName")
    private String contactLastName;
    @Element(name = "ContactPhone")
    private String contactPhone;
    @Element(name = "ContactCurrentAddress")
    private String contactAddress;
    @Element(name = "InjuryWeeks")
    private int injuryWeeks;

    private List<Evaluation> evaluationList;

    public Patient(String identification, String name, String lastName, String documentType, char genre, String address, String phone, String birthday, String province, String municipality, String lane, String contactName, String contactLastName, String contactPhone, String contactAddress, int injuryWeeks) {
        this.UUIDNumber = UUID.randomUUID().toString();
        this.identification = identification;
        this.name = name;
        this.lastName = lastName;
        this.documentType = documentType;
        this.genre = genre;
        this.address = address;
        this.phone = phone;
        this.birthday = birthday;
        this.province = province;
        this.municipality = municipality;
        this.lane = lane;
        this.contactName = contactName;
        this.contactLastName = contactLastName;
        this.contactPhone = contactPhone;
        this.contactAddress = contactAddress;
        this.injuryWeeks = injuryWeeks;
        this.evaluationList = new ArrayList<Evaluation>();
    }

    public Patient(String uuid, String identification, String name, String lastName, String documentType, char genre, String address, String phone, String birthday, String province, String municipality, String lane, String contactName, String contactLastName, String contactPhone, String contactAddress, int injuryWeeks) {
        this.UUIDNumber = uuid;
        this.identification = identification;
        this.name = name;
        this.lastName = lastName;
        this.documentType = documentType;
        this.genre = genre;
        this.address = address;
        this.phone = phone;
        this.birthday = birthday;
        this.province = province;
        this.municipality = municipality;
        this.lane = lane;
        this.contactName = contactName;
        this.contactLastName = contactLastName;
        this.contactPhone = contactPhone;
        this.contactAddress = contactAddress;
        this.injuryWeeks = injuryWeeks;
        this.evaluationList = new ArrayList<Evaluation>();
    }

    public Patient(String uuid, String identification, String name, String lastName) {
        this.UUIDNumber = uuid;
        this.identification = identification;
        this.name = name;
        this.lastName = lastName;
        this.evaluationList = new ArrayList<Evaluation>();
    }

    @Override
    public void ParseAttributes() {
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

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public char getGenre() {
        return genre;
    }

    public void setGenre(char genre) {
        this.genre = genre;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public int getInjuryWeeks() {
        return injuryWeeks;
    }

    public void setInjuryWeeks(int injuryWeeks) {
        this.injuryWeeks = injuryWeeks;
    }

    public List<Evaluation> getEvaluationList() {
        return evaluationList;
    }

    /* Adds an evaluation to the evaluations list */
    public boolean addEvaluation(Evaluation evaluation) {
        return evaluationList.add(evaluation);
    }

    /* Returns the evaluation in the i position of the list */
    public Evaluation getEvaluation(int i) {
        return evaluationList.get(i);
    }

    /* Adds a collection of evaluations to the evaluations list */
    public boolean addAllEvaluations(Collection<? extends Evaluation> evaluations) {
        return evaluationList.addAll(evaluations);
    }
}
