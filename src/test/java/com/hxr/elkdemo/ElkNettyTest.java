package com.hxr.elkdemo;

import com.hxr.elkdemo.entity.User;
import com.hxr.elkdemo.util.ObjEncoder;
import com.hxr.elkdemo.util.ObjectDecoder;
import com.hxr.elkdemo.util.TransportProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.UUID;

public class ElkNettyTest {
    public static void main(String[] args) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    //对象传输处理
                    channel.pipeline().addLast(new ObjectDecoder(TransportProtocol.class));
                    channel.pipeline().addLast(new ObjEncoder(TransportProtocol.class));
                    // 在管道中添加我们自己的接收数据实现方法
                    channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                        }
                    });
                }
            });
            ChannelFuture f = b.connect("127.0.0.1", 7397).sync();
            System.out.println("itstack-demo-netty client start done. ");

            //TransportProtocol tp1 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "kevin", 1, "T0-1", new Date(), "13566668888", "184172133@qq.com", "bj"));
            TransportProtocol tp2 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "chris", 2, "T0-2", new Date(), "13566660001", "huahua@qq.com", "nj"));

            //向服务端发送信息
            //f.channel().writeAndFlush(tp1);
            f.channel().writeAndFlush(tp2);

            f.channel().closeFuture().syncUninterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
