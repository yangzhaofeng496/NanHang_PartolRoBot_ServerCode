package yang.plane.pojo.VO;

import lombok.Data;

/**
 * http获取飞机信息时所需要的VO
 */
@Data
public class PlaneInfoTo3D {

    public String carAreaID;
    public String areaName;
    public String modelAreaID;
    public float r;
    public float x;
    public float y;
}
