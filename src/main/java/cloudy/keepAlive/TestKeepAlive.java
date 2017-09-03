package cloudy.keepAlive;

import cloudy.keepAlive.entity.Request;
import cloudy.keepAlive.entity.User;
import cloudy.keepAlive.netty.KeepAliveClient;

/**
 * Created by 7cc on 2017/9/2
 */
public class TestKeepAlive {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            if(i == 1) Thread.sleep(1000);
            Request request = new Request();
            request.setIdentifier("getUserName");
            request.setContent(new User(7 + i, "7cc" + i, 77 + i));
            Object result = KeepAliveClient.send(request);
            System.out.println(result);
        }
    }
}
