package i2t.cideim.model;

import java.io.Serializable;

import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

/**
 * Created by Juan David.
 * Base class to perform the serialization of data for synchronization.
 */

@Root(name = "data")
@NamespaceList({
        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(reference = "http://www.w3.org/2001/XMLSchema", prefix = "xsd")})
public abstract class DataXml implements Serializable {

    private static final long serialVersionUID = 1L;

    public DataXml() {
    }

    public abstract void ParseAttributes();

}
