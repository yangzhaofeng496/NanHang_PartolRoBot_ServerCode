package yang.plane.api;
import yang.plane.pojo.VO.PlaneInfoTo3D;

import java.util.List;

public interface PlaneInitInfoIntf {

    public  List<PlaneInfoTo3D> getPlaneInitInfo(String planeID);


}
