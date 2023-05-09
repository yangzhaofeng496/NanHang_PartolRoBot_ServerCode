//package yang.robot.config.ros;
//
//import com.alibaba.fastjson.JSON;
//import edu.wpi.rail.jrosbridge.callback.TopicCallback;
//import edu.wpi.rail.jrosbridge.messages.Message;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//
//@Component
//public class GnssCallBack implements TopicCallback {
//    private static final Logger logger = LoggerFactory.getLogger(GnssCallBack.class);
//
//    @Override
//    public void handleMessage(Message message) {
//        try {
//            Map maps = (Map) JSON.parse(message.toString());   //message即该话题的广播消息
//
//            //todo:这里对map做逻辑处理
//            logger.info(maps.toString());
//        } catch (Exception e) {
//            logger.error(e.toString());
//        }
//    }
//
//
//}