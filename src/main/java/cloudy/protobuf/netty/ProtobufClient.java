package cloudy.protobuf.netty;

import cloudy.protobuf.entity.RequestMsgProbuf.RequestMsg;
import cloudy.protobuf.entity.ResponseMsgProbuf.ResponseMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.util.AttributeKey;

/**
 * Created by 7cc on 2017/9/3
 */
public class ProtobufClient {

    public static Bootstrap bootstrap;
    public static ChannelFuture channelFuture;

    static {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        try {
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            sc.pipeline().addLast(new ProtobufDecoder(ResponseMsg.getDefaultInstance()));
                            sc.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            sc.pipeline().addLast(new ProtobufEncoder());
                            sc.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    if (msg instanceof ResponseMsg) {
                                        ResponseMsg responseMsg = (ResponseMsg) msg;
                                        ctx.channel().attr(AttributeKey.valueOf("7cc")).set(responseMsg.getResponse());
                                        ctx.close();
                                    }
                                }
                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    cause.printStackTrace();
                                    ctx.close();
                                }
                            });

                        }
                    });
            channelFuture = bootstrap.connect("localhost", 8765).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Object sendMsg(RequestMsg requestMsg) throws Exception {
        channelFuture.channel().writeAndFlush(requestMsg);
        channelFuture.channel().closeFuture().sync();
        return channelFuture.channel().attr(AttributeKey.valueOf("7cc")).get();
    }

}
