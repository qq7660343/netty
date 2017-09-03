package cloudy.keepAlive.netty;

import cloudy.keepAlive.entity.Request;
import cloudy.keepAlive.entity.Response;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by 7cc on 2017/9/2
 */
public class KeepAliveClient {
    public static ChannelFuture channelFuture;

    static {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                            sc.pipeline().addLast(new StringDecoder());
                            sc.pipeline().addLast(new ClientChannelHandler());
                            sc.pipeline().addLast(new StringEncoder());
                        }
                    });
            channelFuture = bootstrap.connect("localhost", 8765).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object send(Request request) {
        try {
            channelFuture.channel().write(JSONObject.toJSONString(request));
            ConcurrentResponse concurrentResponse = new ConcurrentResponse(request);
            channelFuture.channel().writeAndFlush("\r\n");
            Response response = concurrentResponse.get(3);
            return response;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
