package cloudy.protobuf;

import cloudy.protobuf.entity.RequestMsgProbuf.RequestMsg;
import cloudy.protobuf.entity.UserProbuf.User;
import cloudy.protobuf.netty.ProtobufClient;
import com.google.protobuf.ByteString;

/**
 * Created by 7cc on 2017/9/3
 */
public class TestProtobuf {

    public static void main(String[] args) throws Exception {
        User userProtobuf = User.newBuilder().setId(66).setUserName("7cc").setPhone("187").build();
        RequestMsg requestMsg = RequestMsg.newBuilder().setCmd("getRemoteUser").setRequestParam(userProtobuf.toByteString()).build();
        ByteString response = (ByteString) ProtobufClient.sendMsg(requestMsg);
        User user = User.parseFrom(response);
        System.out.println(user.getId());
        System.out.println(user.getUserName());
        System.out.println(user.getPhone());
        System.out.println("=======================================");
        Thread.sleep(2000);
        response = (ByteString) ProtobufClient.sendMsg(requestMsg);
        user = User.parseFrom(response);
        System.out.println(user.getId());
        System.out.println(user.getUserName());
        System.out.println(user.getPhone());
        response = (ByteString) ProtobufClient.sendMsg(requestMsg);
        user = User.parseFrom(response);
        System.out.println(user.getId());
        System.out.println(user.getUserName());
        System.out.println(user.getPhone());
    }
}
