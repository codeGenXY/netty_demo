package cn.xy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * Created by xiaoyuan.xy on 2016/8/23.
 */
public class EchoClient {

    private String host;
    private Integer port;

    public EchoClient(String host,Integer port){
        this.host=host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception{
        new EchoClient("localhost",8080).start();
    }

    public void start()throws Exception{
        final EchoClientHandler echoClientHandler = new EchoClientHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(echoClientHandler);
                        }
                    });
            ChannelFuture f =bootstrap.connect().sync();
            Channel ch = f.channel();
            Integer i=0;
            while(true){
                ch.writeAndFlush(Unpooled.copiedBuffer("hello"+i, CharsetUtil.UTF_8));
                Thread.sleep(1000);
                i++;
            }
            //f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

}
