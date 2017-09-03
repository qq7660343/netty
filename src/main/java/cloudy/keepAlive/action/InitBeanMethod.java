package cloudy.keepAlive.action;

import cloudy.keepAlive.entity.Action;
import cloudy.keepAlive.entity.BeanMethod;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by 7cc on 2017/9/2
 */
@Component
public class InitBeanMethod implements ApplicationListener<ContextStartedEvent>, Ordered{

    @Override
    public void onApplicationEvent(ContextStartedEvent contextStartedEvent) {
        Map<String, Object> mapBeans = contextStartedEvent.getApplicationContext().getBeansWithAnnotation(Controller.class);
        mapBeans.keySet().forEach(System.out::print);
        mapBeans.values().forEach(bean -> {
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Action.class)) {
                    BeanMethod beanMethod = new BeanMethod(bean ,method);
                    Action action = method.getAnnotation(Action.class);
                    String identifier = action.value();
                    ParamHandler.identifierBeanMethod.put(identifier, beanMethod);
                }
            }
        });

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
