package cloudy.keepAlive.netty;

import cloudy.keepAlive.entity.Response;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by 7cc on 2017/9/2
 */
public class ClientChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof String) {
            String msgString = msg.toString();
            if (msgString.equals("server ping")) {
                ctx.writeAndFlush("client ping\r\n");
                System.out.println("heart ping...");
                return;
        }
//        System.out.println(String.format("client receive response msg = %s", msgString));
        Response response = JSONObject.parseObject(msgString, Response.class);
        ConcurrentResponse.receive(response);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
