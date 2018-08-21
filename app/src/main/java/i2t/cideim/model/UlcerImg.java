package i2t.cideim.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.UUID;


/**
 * Created by Domiciano Rincon
 * Represents an UIcerImg. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "UlcerImgXml")
public class UlcerImg extends DataXml {

    private static final long serialVersionUID = 1L;
    //@Attribute(name = "xsi:type")
    private String classname = "UIcerImgXml";

    @Element(name = "Id", required = false)
    private String uuid;

    @Element(name = "BodyLocation", required = false)
    private String bodyLocation;

    @Element(name = "ImgDate", required = false)
    private Date imgDate;

    @Element(name = "ImgFormat", required = false)
    private String imgFormat;

    @Element(name = "ImgUUID", required = false)
    private String imgUUID;

    @Element(name = "InjuriesPerLocation", required = false)
    private String injuriesPerLocation;

    public UlcerImg() {
        this.uuid = UUID.randomUUID().toString();
    }

    public UlcerImg(String bodyLocation, Date imgDate, String imgFormat, String imgUUID, String injuriesPerLocation) {
        this.uuid = UUID.randomUUID().toString();
        this.bodyLocation = bodyLocation;
        this.imgDate = imgDate;
        this.imgFormat = imgFormat;
        this.imgUUID = imgUUID;
        this.injuriesPerLocation = injuriesPerLocation;
    }

    public UlcerImg(String uuid, String bodyLocation, Date imgDate, String imgFormat, String imgUUID, String injuriesPerLocation) {
        this.uuid = uuid;
        this.bodyLocation = bodyLocation;
        this.imgDate = imgDate;
        this.imgFormat = imgFormat;
        this.imgUUID = imgUUID;
        this.injuriesPerLocation = injuriesPerLocation;
    }

    public String getInjuriesPerLocation() {
        return injuriesPerLocation;
    }

    public void setInjuriesPerLocation(String injuriesPerLocation) {
        this.injuriesPerLocation = injuriesPerLocation;
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



    public Date getImgDate() {
        return imgDate;
    }

    public void setImgDate(Date imgDate) {
        this.imgDate = imgDate;
    }

    public String getImgFormat() {
        return imgFormat;
    }

    public void setImgFormat(String imgFormat) {
        this.imgFormat = imgFormat;
    }

    public String getImgUUID() {
        return imgUUID;
    }

    public void setImgUUID(String imgUUID) {
        this.imgUUID = imgUUID;
    }


}
