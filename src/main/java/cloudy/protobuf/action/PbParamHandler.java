package cloudy.protobuf.action;

import cloudy.protobuf.entity.PbBeanMethod;
import cloudy.protobuf.entity.RequestMsgProbuf.RequestMsg;
import com.google.protobuf.ByteString;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 7cc on 2017/9/3
 */
public class PbParamHandler {

    public static final Map<String, PbBeanMethod> cmdPbBeanMethod = new HashMap<>();

    public static Object execute(Object request) throws Exception {
        if (request instanceof RequestMsg) {
            RequestMsg requestMsg = (RequestMsg) request;
            String cmd = requestMsg.getCmd();
            PbBeanMethod BeanMethod = cmdPbBeanMethod.get(cmd);
            Object bean = BeanMethod.getBean();
            Method method = BeanMethod.getMethod();
            Class<?> paramObject = method.getParameterTypes()[0];
            Constructor<?>[] constructors = paramObject.getDeclaredConstructors();
            Constructor constructor = null;
            for(Constructor c : constructors) {
                if(c.getParameterTypes()[0].getName().equals("boolean")) {
                    constructor = c;
                }
            }
            if (constructor != null) {
                constructor.setAccessible(true);
            }
            Object paramBean = constructor.newInstance(true);
            ByteString byteString = requestMsg.getRequestParam();
            Method paramMethod = paramObject.getMethod("parseFrom", ByteString.class);
            paramBean = paramMethod.invoke(paramBean, byteString);
            return method.invoke(bean, paramBean);
        }

        return null;
    }
}
