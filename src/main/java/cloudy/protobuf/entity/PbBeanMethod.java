package cloudy.protobuf.entity;

import java.lang.reflect.Method;

/**
 * Created by 7cc on 2017/9/2
 */
public class PbBeanMethod {
    private Object bean;
    private Method method;

    public PbBeanMethod() {
    }

    public PbBeanMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
