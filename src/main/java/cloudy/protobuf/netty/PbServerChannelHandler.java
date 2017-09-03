package cloudy.protobuf.netty;

import cloudy.protobuf.action.PbParamHandler;
import cloudy.protobuf.entity.RequestMsgProbuf.RequestMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by 7cc on 2017/9/3
 */
public class PbServerChannelHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestMsg requestMsg = (RequestMsg) msg;
        String cmd = requestMsg.getCmd();
        System.out.println(String.format("server received client request cmd = %s", cmd));
        Object response = PbParamHandler.execute(requestMsg);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
