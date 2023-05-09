package yang.plane.service;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yang.plane.api.PlaneInitInfoIntf;
import yang.plane.mapper.PlaneinfoMapper;
import yang.plane.pojo.PO.PlaneinfoPO;
import yang.plane.pojo.VO.PlaneInfoTo3D;

import java.util.List;

@Service
public class PlainInitInfoService implements PlaneInitInfoIntf {

    @Autowired
    PlaneinfoMapper planeinfoMapper;

    /**
     * 根据飞机ID获取对应的初始化信息
     * @param planeID
     * @return
     */
    @Override
    public List<PlaneInfoTo3D> getPlaneInitInfo(String planeID) {

        PlaneinfoPO planeinfoPO = planeinfoMapper.selectByPrimaryKey(planeID);
        List<PlaneInfoTo3D> planeInfoTo3Ds = JSON.parseArray(planeinfoPO.getInformation(),PlaneInfoTo3D.class) ;
        return planeInfoTo3Ds;
    }
}
