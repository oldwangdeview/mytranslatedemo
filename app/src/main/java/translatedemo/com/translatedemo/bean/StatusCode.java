package translatedemo.com.translatedemo.bean;

/**
 * Created by oldwang on 2019/1/2 0002.
 */


public class StatusCode<T> {
    private int code;// 状态码  状态码状态 0成功true 1失败false
    private boolean flag = false;//
    private String msg;// 状态码值
    private String detailMsg;// 状态码详细值
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int number) {
        this.code = number;
    }

    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StatusCode{" +
                "number='" + code + '\'' +
                ", flag=" + flag +
                ", msg='" + msg + '\'' +
                ", detailMsg='" + detailMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
