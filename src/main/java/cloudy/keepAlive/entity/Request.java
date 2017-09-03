package cloudy.keepAlive.entity;

import java.io.Serializable;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by 7cc on 2017/9/2
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 5837438119247137126L;

    private String identifier;
    private Object content;
    private final long id;
    private static final LongAdder idFactory = new LongAdder();

    public Request() {
        idFactory.increment();
        id = idFactory.longValue();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public static LongAdder getIdFactory() {
        return idFactory;
    }
}
