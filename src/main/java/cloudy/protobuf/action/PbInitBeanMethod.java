package cloudy.protobuf.action;

import cloudy.protobuf.entity.PbBeanMethod;
import cloudy.protobuf.entity.Remote;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by 7cc on 2017/9/3
 */
@Component
public class PbInitBeanMethod implements ApplicationListener<ContextStartedEvent>, Ordered{

    @Override
    public void onApplicationEvent(ContextStartedEvent contextStartedEvent) {
        Map<String, Object> mapBeans = contextStartedEvent.getApplicationContext().getBeansWithAnnotation(Controller.class);
        mapBeans.values().forEach(bean -> {
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Remote.class)) {
                    Remote remote = method.getAnnotation(Remote.class);
                    String cmd = remote.value();
                    PbBeanMethod pbBeanMethod = new PbBeanMethod(bean, method);
                    PbParamHandler.cmdPbBeanMethod.put(cmd, pbBeanMethod);
                }
            }
        });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
