package yang.plane.enumCustom;

public enum MsgType {

    RealTimeNumMsg(1, "实时数字信息"),
    RealTimeImageMsg(2, "实时图片信息"),
    ControlMsg(3, "按钮控制信息"),
    ControlFeedback(4, "控制反馈");

    public final Integer type;
    public final String value;

    MsgType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
