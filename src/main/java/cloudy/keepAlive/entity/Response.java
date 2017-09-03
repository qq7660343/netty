package cloudy.keepAlive.entity;

/**
 * Created by 7cc on 2017/9/2
 */
public class Response {
    private long id;
    private Object content;
    private int status;
    private String msg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", content=" + content +
                ", status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }
}
