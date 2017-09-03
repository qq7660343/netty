package cloudy.keepAlive.entity;

import java.io.Serializable;

/**
 * Created by 7cc on 2017/9/2
 */
public class RequestParam implements Serializable{

    private static final long serialVersionUID = -643876523883084067L;

    private String identifier;
    private Object content;
    private long id;

    public RequestParam() {
    }

    public RequestParam(String identifier, Object content, long id) {
        this.identifier = identifier;
        this.content = content;
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
