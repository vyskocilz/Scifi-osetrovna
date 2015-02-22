package data.system;

import java.io.Serializable;
import java.util.Date;

public class Login implements Serializable {
    private Date datum;
    private String clientName;
    private String version;

    public Login() {

    }

    public Login(Date datum, String clientName, String version) {
        this.clientName = clientName;
        this.datum = datum;
        this.version = version;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
