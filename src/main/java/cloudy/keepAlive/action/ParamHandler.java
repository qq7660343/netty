package cloudy.keepAlive.action;

import cloudy.keepAlive.entity.BeanMethod;
import cloudy.keepAlive.entity.RequestParam;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 7cc on 2017/9/2
 */
public class ParamHandler {

    public static final Map<String, BeanMethod> identifierBeanMethod = new HashMap<>();

    public static Object execute(Object request) throws Exception {
        RequestParam requestParam = (RequestParam) request;
        BeanMethod beanMethod = identifierBeanMethod.get(requestParam.getIdentifier());
        if(null == beanMethod) {
            throw new RuntimeException(String.format("not found identifier = %s", requestParam.getIdentifier()));
        }
        Object bean = beanMethod.getBean();
        Method method = beanMethod.getMethod();
        Class<?> paramType = method.getParameterTypes()[0];
        Object paramObject = JSONObject.parseObject(JSONObject.toJSONString(requestParam.getContent()), paramType);
        return method.invoke(bean, paramObject);
    }
}
