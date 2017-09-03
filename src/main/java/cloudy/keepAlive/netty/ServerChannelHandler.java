package cloudy.keepAlive.netty;

import cloudy.keepAlive.action.ParamHandler;
import cloudy.keepAlive.entity.RequestParam;
import cloudy.keepAlive.entity.Response;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.nio.charset.Charset;

/**
 * Created by 7cc on 2017/9/2
 */
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//            System.out.println("server received msg==========================");
            if (msg instanceof ByteBuf) {
            ByteBuf req = (ByteBuf) msg;
            String content = req.toString(Charset.defaultCharset());
//            System.out.println(content);

            RequestParam requestParam = JSONObject.parseObject(content, RequestParam.class);
            Object result = ParamHandler.execute(requestParam);

            Response response = new Response();
            response.setId(requestParam.getId());
            response.setContent(result);
            ctx.channel().write(JSONObject.toJSONString(response));
            ctx.channel().writeAndFlush("\r\n");
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    ctx.channel().writeAndFlush("server ping\r\n");
                    break;
                default:
                    throw new RuntimeException(String.format("unknown state %s", event.state()));
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
