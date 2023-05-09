//package yang.robot.config.ros;
//
//import edu.wpi.rail.jrosbridge.Ros;
//import edu.wpi.rail.jrosbridge.Topic;
//import edu.wpi.rail.jrosbridge.handler.RosHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.web.bind.annotation.RestController;
//
//
//import javax.annotation.PostConstruct;
//import javax.websocket.Session;
//
//
//@RestController
//    public class RosController implements RosHandler {
//    private static final Logger logger = LoggerFactory.getLogger(RosController.class);
//
//
//    //properties配置文件配置
//
//    //ros服务器ip
//    @Value("${ros.ip}")
//    private String ip;
//
//    //指定订阅的话题名称/类型
//    @Value("${ros.topic.gnss.name}")
//    private String gnssName;
//    @Value("${ros.topic.gnss.type}")
//    private String gnssType;
//
//
//
//    //实现了话题消息接收（可对消息进行逻辑处理）
//    @Autowired
//    private GnssCallBack gnssCallBack;
//
//
//    Ros ros = null;
//    boolean connect = false;
//
//    /**
//     * ros服务器故障断开每隔五秒重连
//     */
//    @Scheduled(cron = "*/5 * * * * ?")
//    void reconnect() {
//        if (connect == false) {
//            logger.error("未连接服务！正在重连ros服务器！");
//            init();
//        }
//    }
//
//    @PostConstruct
//    void init() {
//        try {
//            ros = new Ros(ip);
//            connect = ros.connect();
//            if (connect) {
//                Topic gnss_pose = new Topic(ros, gnssName, gnssType);
//
//                gnss_pose.subscribe(gnssCallBack);    //开始订阅话题
//
//                ros.addRosHandler(this);    //监听器监听ros连接状态
//            }
//
//        } catch (Exception e) {
//            logger.error("ros服务器连接错误:" + e.toString());
//            connect = false;
//        }
//
//    }
//
//    @Override
//    public void handleConnection(Session session) {
//        logger.info("ros已连接：" + session.getId());
//    }
//
//    @Override
//    public void handleDisconnection(Session session) {
//        logger.error("断开ros连接：" + session.getId());
//        connect = false;
//    }
//
//    @Override
//    public void handleError(Session session, Throwable t) {
//        logger.error("ros连接失败：" + session.getId() + "-----失败原因：" + t.toString());
//        connect = false;
//    }
//}