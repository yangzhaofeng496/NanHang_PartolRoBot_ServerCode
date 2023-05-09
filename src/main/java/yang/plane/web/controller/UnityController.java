package yang.plane.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yang.plane.api.PlaneInitInfoIntf;
import yang.plane.config.websocket.WebSocketServer;
import yang.plane.pojo.VO.PlaneInfoTo3D;
import yang.plane.util.BASE64Encoder;
import yang.plane.util.JSONResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/unity3d")
public class UnityController {

    @Autowired
    PlaneInitInfoIntf planeInitInfoIntf;

    @PostMapping("/updateSpeed")
    public void UpdateCarSpeed() throws IOException {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("3");
        WebSocketServer .sendMessage("1",hashSet);
    }

        @PostMapping("/imgSave")
    public void uploadToSave(MultipartFile fileUpload){
        //获取文件名
        String fileName = fileUpload.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //重新生成文件名
        fileName = UUID.randomUUID()+suffixName;
        //指定本地文件夹存储图片，写到需要保存的目录下
        String filePath = "/Users/local/apache-tomcat-9.0.58/webapps/PlaneImage/";
        try {
            //将图片保存到static文件夹里
            fileUpload.transferTo(new File(filePath+fileName));
            HashSet<String> hashSet = new HashSet<>();
            hashSet.add("3");
            WebSocketServer.sendMessage(fileName,hashSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/imgBytes")
    public void uploadToBytes(MultipartFile fileUpload) throws IOException {

        File f = null;
        f = File.createTempFile("tmp", null);
        fileUpload.transferTo(f);
        f.deleteOnExit();     //使用完成删除文件
        FileInputStream inputFile = new FileInputStream(f);
        String base64 = null;
        byte[] buffer = new byte[(int) f.length()];
        inputFile.read(buffer);
        inputFile.close();
        base64 = new BASE64Encoder().encode(buffer);
        //System.out.println(base64);
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("3");
        WebSocketServer.sendMessage(base64,hashSet);
    }

    @GetMapping("/sendTest")
    public void SendTest() throws IOException {

        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("1");
        WebSocketServer.sendMessage("test",hashSet);
    }


    @GetMapping("/getInitInfo")
    public JSONResult getInitInfo(String planeID) throws IOException {
        List<PlaneInfoTo3D> planeInfoTo3Ds = planeInitInfoIntf.getPlaneInitInfo(planeID);
        return JSONResult.ok(planeInfoTo3Ds);

    }


}