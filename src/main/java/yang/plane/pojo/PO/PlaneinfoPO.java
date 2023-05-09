package yang.plane.pojo.PO;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "planeInfo")
public class PlaneinfoPO {
    @Id
    private String id;

    @Column(name = "Information")
    private String information;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Information
     */
    public String getInformation() {
        return information;
    }

    /**
     * @param information
     */
    public void setInformation(String information) {
        this.information = information;
    }
}