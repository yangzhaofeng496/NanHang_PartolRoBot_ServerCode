package yang.plane.pojo.VO;

/**
 * http上传飞机信息时所需要的VO
 */
public class PlaneInitVO {

    String planeID;
    String initMsg;

    public String getPlaneID() {
        return planeID;
    }

    public void setPlaneID(String planeID) {
        this.planeID = planeID;
    }

    public String getInitMsg() {
        return initMsg;
    }

    public void setInitMsg(String initMsg) {
        this.initMsg = initMsg;
    }
}
