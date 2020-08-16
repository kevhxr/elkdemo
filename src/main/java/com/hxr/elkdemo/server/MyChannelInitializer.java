package com.hxr.elkdemo.server;

import com.hxr.elkdemo.util.ObjEncoder;
import com.hxr.elkdemo.util.ObjectDecoder;
import com.hxr.elkdemo.util.TransportProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    MyServerHandler myServerHandler;

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 基于换行符号
        channel.pipeline().addLast(new ObjectDecoder(TransportProtocol.class));
        channel.pipeline().addLast(new ObjEncoder(TransportProtocol.class));
        // 在管道中添加我们自己的接收数据实现方法
        channel.pipeline().addLast(myServerHandler);
    }
}
