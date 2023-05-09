package yang.plane.pojo;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "testtable")
public class DemoClass {

    @Column(name = "osex")
    private float osex;

    @Column(name = "osey")
    private float osey;


    public Float getOsex() {
        return osex;
    }

    public void setOsex(float osex) {
        this.osex = osex;
    }

    public Float getOsey() {
        return osey;
    }

    public void setOsey(float osey) {
        this.osey = osey;
    }
}
