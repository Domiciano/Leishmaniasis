package i2t.cideim.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.UUID;


/**
 * Created by Domiciano Rincon
 * Represents an UIcerImg. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "HisopoXml")
public class Hisopo extends DataXml {

    private static final long serialVersionUID = 1L;
    //@Attribute(name = "xsi:type")
    private String classname = "UIcerImgXml";

    @Element(name = "Id", required = false)
    private String uuid;

    @Element(name = "BodyLocation", required = false)
    private String bodyLocation;

    @Element(name = "Date")
    private Date date; // dd/MM/yyyy

    @Element(name = "Muestas")
    private int muestras; // dd/MM/yyyy

    public Hisopo() {
        this.uuid = UUID.randomUUID().toString();
    }

    public Hisopo(String uuid, String bodyLocation, Date date, int muestras) {
        this.uuid = uuid;
        this.bodyLocation = bodyLocation;
        this.date = date;
        this.muestras = muestras;
    }

    @Override
    public void ParseAttributes() {

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBodyLocation() {
        return bodyLocation;
    }

    public void setBodyLocation(String bodyLocation) {
        this.bodyLocation = bodyLocation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMuestras() {
        return muestras;
    }

    public void setMuestras(int muestras) {
        this.muestras = muestras;
    }
}
