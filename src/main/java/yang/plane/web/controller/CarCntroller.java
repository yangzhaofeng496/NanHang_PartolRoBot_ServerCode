package yang.plane.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import yang.plane.mapper.PlaneinfoMapper;
import yang.plane.pojo.PO.PlaneinfoPO;
import yang.plane.pojo.VO.PlaneInitVO;
import yang.plane.util.JSONResult;

@RestController
@RequestMapping("/car")
public class CarCntroller {

    @Autowired
    PlaneinfoMapper planeinfoMapper;


    @PostMapping("/initInfoUpload")
    public JSONResult InitInfoUpload(PlaneInitVO planeInitVO)
    {
        PlaneinfoPO planeinfoPO = new PlaneinfoPO();
        planeinfoPO.setId(planeInitVO.getPlaneID());
        planeinfoPO.setInformation(planeInitVO.getInitMsg());
        int i =0;
        if(planeinfoMapper.selectByPrimaryKey(planeInitVO.getPlaneID())!=null)
        {
            Example example = new Example(PlaneinfoPO.class);
            i = planeinfoMapper.updateByPrimaryKeySelective(planeinfoPO);

        }else{
            i = planeinfoMapper.insert(planeinfoPO);
        }
        if(i==1)
        {
            return JSONResult.ok("更新成功");
        }else {
            return JSONResult.errorMsg("更新失败");
        }
    }

}
