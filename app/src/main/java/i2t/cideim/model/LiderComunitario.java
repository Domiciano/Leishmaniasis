package i2t.cideim.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Leonardo and Juan David.
 * Represents an user (former líder comunitario). Has the required annotations to serialize the data
 * and send it to the server.
 */

@Root(name = "data")
public class LiderComunitario extends DataXml {

    private static final long serialVersionUID = 1L;
    @Attribute(name = "xsi:type")
    private String classname = "LiderComunitarioXml";
    @Element(name = "Cedula")
    private String identification; // Password
    @Element(name = "Name")
    private String name; // Username
    @Element(name = "LastName")
    private String lastName;
    @Element(name = "Gender")
    private char genre;

    private List<Patient> patientList;

    public LiderComunitario(String identification, String name, String lastName, char genre) {
        this.identification = identification;
        this.name = name;
        this.lastName = lastName;
        this.genre = genre;
        this.patientList = new ArrayList<Patient>();
    }

    public LiderComunitario(String identification, String name) {
        this.identification = identification;
        this.name = name;
        this.patientList = new ArrayList<Patient>();
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

    public char getGenre() {
        return genre;
    }

    public void setGenre(char genre) {
        this.genre = genre;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    /* Adds a patient to the patients list */
    public boolean addPatient(Patient patient) {
        return patientList.add(patient);
    }

    /* Returns the patient in the i position of the list */
    public Patient getPatient(int i) {
        return patientList.get(i);
    }

    /* Adds a collection of patients to the patients list */
    public boolean addAllPatients(Collection<? extends Patient> patients) {
        return patientList.addAll(patients);
    }
}
