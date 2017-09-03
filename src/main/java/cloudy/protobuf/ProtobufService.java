package cloudy.protobuf;

import cloudy.protobuf.entity.Remote;
import cloudy.protobuf.entity.ResponseMsgProbuf.ResponseMsg;
import cloudy.protobuf.entity.UserProbuf.User;
import org.springframework.stereotype.Controller;

/**
 * Created by 7cc on 2017/9/3
 */
@Controller
public class ProtobufService {

    @Remote("getRemoteUser")
    public Object getRemoteUser(User user) {
        return ResponseMsg.newBuilder().setResponse(user.toBuilder()
                .setUserName(user.getUserName() + " - remote")
                .setId(777)
                .setPhone(user.getPhone() + "- remote").build().toByteString()).build();
    }
}
