package cloudy.keepAlive;

import cloudy.keepAlive.entity.Action;
import cloudy.keepAlive.entity.User;
import org.springframework.stereotype.Controller;

/**
 * Created by 7cc on 2017/9/2
 */
@Controller
public class UserService {

    @Action("getUserName")
    public Object getUserName(User user) {
//        System.out.println(user);
        return String.format(" %s - %s - %s ", user.getId(), user.getName() , user.getAge());
    }
}
